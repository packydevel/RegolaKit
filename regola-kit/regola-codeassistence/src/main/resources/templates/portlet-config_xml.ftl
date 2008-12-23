<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:webflow="http://www.springframework.org/schema/webflow-config"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
           http://www.springframework.org/schema/webflow-config
           http://www.springframework.org/schema/webflow-config/spring-webflow-config-2.0.xsd">

	<!-- Maps portlet modes to handlers -->	
	<bean id="portletModeHandlerMapping" class="org.springframework.web.portlet.handler.PortletModeHandlerMapping">
		<property name="portletModeMap">
			<map>
				<entry key="view">
					<bean class="org.regola.webapp.flow.mvc.ViewFlowHandler" />
				</entry>
			</map>
		</property>
	</bean>

	<!-- Maps logical view names selected by the url filename controller to .jsp view templates within the /WEB-INF directory -->	
	<bean id="internalJspViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="prefix" value="/WEB-INF/" />
		<property name="suffix" value=".jsp" />
	</bean>
	
	<!-- Enables FlowHandlers -->
	<bean class="org.springframework.webflow.mvc.portlet.FlowHandlerAdapter">
		<property name="flowExecutor" ref="flowExecutor"/>
	</bean>

	<!-- Executes flows: the central entry point into the Spring Web Flow system -->
	<webflow:flow-executor id="flowExecutor">
		<webflow:flow-execution-listeners>
			<webflow:listener ref="hibernateFlowExecutionListener" />
		</webflow:flow-execution-listeners>
	</webflow:flow-executor>
	
	<!-- The registry of executable flow definitions -->
	<webflow:flow-registry id="flowRegistry">
		<webflow:flow-location path="/WEB-INF/flows/${portlet_name}/main.xml" />
	</webflow:flow-registry>	
		
	<!-- Serve per gestire la transazione all'interno di webflow -->
	<bean id="hibernateFlowExecutionListener" class="org.springframework.webflow.persistence.HibernateFlowExecutionListener">
		<constructor-arg index="0">
			<ref bean="sessionFactory"/>
		</constructor-arg>
        <constructor-arg index="1">
        	<ref bean="transactionManager"/>
        </constructor-arg>
   </bean>
		
</beans>