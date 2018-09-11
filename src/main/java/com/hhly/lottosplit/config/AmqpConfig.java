package com.hhly.lottosplit.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.rabbitmq.consumer.SplitTicketMessageListener;
import com.hhly.lottosplit.rabbitmq.provider.impl.SendTicketMessageProvider;
import com.hhly.lottosplit.service.LotteryTypeService;
import com.hhly.lottosplit.service.OrderService;
import com.hhly.lottosplit.service.SplitTicketService;
import com.hhly.lottosplit.service.TicketInfoService;
import com.hhly.lottosplit.thread.SplitThread;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.rabbitmq.client.Channel;

/**
 * rabbitmq配置类
 * @author wul687
 * @date 2018-07-25
 */
@Configuration
public class AmqpConfig {
	@Value("${spring.rabbitmq.host}")
	private String ADDRESSES;
	@Value("${spring.rabbitmq.username}")
	private String USER_NAME;
	@Value("${spring.rabbitmq.password}")
	private String PASS_WORD;
	@Value("${spring.rabbitmq.port}")
	private String PORT;
	@Value("${spring.rabbitmq.virtualHost}")
	private String VIRTUAL_HOST;
	@Value("${queuename}")
	private String QUEUENAME;
	
	private Logger logger = LoggerFactory.getLogger(AmqpConfig.class);
	@Autowired
    OrderService orderService;
	@Autowired
	SplitTicketService splitTicketService;
	@Autowired
	LotteryTypeService lotteryTypeService; 
	@Autowired
	SendTicketMessageProvider messageProvider;
	@Autowired
	TicketInfoService ticketInfoService;
	
	
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(ADDRESSES,Integer.valueOf(PORT));
		connectionFactory.setUsername(USER_NAME);
		connectionFactory.setPassword(PASS_WORD);
		connectionFactory.setVirtualHost(VIRTUAL_HOST);
		connectionFactory.setPublisherConfirms(true); //必须要设置
		return connectionFactory;
	}
	
	@Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }
	

    /**
     * 配置消息交换机
     * 针对消费者配置
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange defaultExchange() {
    	Map<String,Object> args = new HashMap<String, Object>();  
        args.put("x-max-priority",10); //队列的属性参数 有10个优先级别
        return new DirectExchange(null, true, false, args);
    }
	
	@Bean
    public Queue queue() {
        return new Queue(QUEUENAME, true); //队列持久
    }
	
	/**
     * 将消息队列1与交换机绑定
     * 针对消费者配置
     *
     * @return
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(QUEUENAME);
    }

	@Bean
    public SimpleMessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(queue());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        container.setMessageListener(new ChannelAwareMessageListener() {
 
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
            	try {
        			//消息的标识，false只确认当前一个消息收到，true确认所有consumer获得的消息
        			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); 
                	//内容格式(1:支付完成,2:完成当前期追号订单,格式:订单编号1,订单编号2#1)
                	//渠道来源(1:支付完成,2:完成当前期追号订单,3:未知来源)
                	String orderCode = new String(message.getBody(),"UTF-8");
                	if(ObjectUtil.isBlank(orderCode)){
                		return;
                	}
                	//1.返回订单编号的数组
                	String[] orderCodes = SplitTicketUtil.checkIn(orderCode);
                	if(orderCodes!=null){
                		//2.根据订单编号查询订单集合信息
                		List<OrderInfoBO> orderInfoBOs = orderService.getOrderInfos(Arrays.asList(orderCodes));
                		Map<Integer,Set<String>> setMap = new HashMap<>();
                		List<Integer> lotteryCodes = new ArrayList<>();
                		SplitTicketUtil.getLotteryCodeOrSystemCodes(orderInfoBOs, setMap, lotteryCodes);
                		for(OrderInfoBO orderInfoBO : orderInfoBOs){
                			//3.查询赛事信息，返回map集合  map<赛事编号,赛事信息表>
                			Map<String,SportAgainstInfoBO> sportAgainstInfoMap = lotteryTypeService.findSportAgainstInfoS(setMap.get(orderInfoBO.getLotteryCode()),orderInfoBO.getLotteryCode());
                			//4.查询彩种信息，返回map集合 map<彩种ID,彩种信息表>
                			Map<Integer,LotteryTypeBO> map = lotteryTypeService.LotteryTypeBoMap(lotteryCodes);
                			//5.多线程
                			for(OrderInfoBO infoBO : orderInfoBOs){
                				SplitThread splitThread = new SplitThread(splitTicketService, infoBO, map, sportAgainstInfoMap, messageProvider, orderService, ticketInfoService);
                				SplitTicketUtil.FIXED_THREAD_POOL.execute(splitThread);
                			}
                		}
                	}
                } catch (Exception e) {
                    logger.error(e.getMessage(),e);
                }
            }
        });
        return container;
    }


}
