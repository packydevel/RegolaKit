package org.regola.codeassistence.generator;

import freemarker.template.Template;
import org.regola.codeassistence.Environment;
import org.regola.codeassistence.VariablesBuilder;


public class ListManagedBeanGenerator extends AbstractGenerator
{
	private static final String LIST_TEMPLATE = "listMBean.ftl";
	private static final String SPRING_BEAN_TEMPLATE = "listMBeanSpringBean.ftl";

	public void generate(Environment env, VariablesBuilder pb)
	{
		Template template = env.getTemplate(LIST_TEMPLATE);	  
		env.addJavaSource((String) pb.getParameters().get("mbean_package")
				, (String) pb.getParameters().get("mbean_list_name")
				, template
				, pb.getParameters());
		
		template = env.getTemplate(SPRING_BEAN_TEMPLATE);
		String beanId = VariablesBuilder.camelNotation((String)pb.getParameters().get("mbean_list_name"));
		env.addSpringBeansToApplicationContext( beanId, template, pb.getParameters());
	}

	public boolean existsArtifact(Environment env, VariablesBuilder pb) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public ListManagedBeanGenerator() {
		name =  "list-handler";
	}

	public String getDisplayName() {

		return "JSF lit managed bean";
	}
	
	public String getDescription() {
		return "Produces a JSF list managed bean.";

	}
}
