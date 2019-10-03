package com.balicamp.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;
import org.springframework.util.ClassUtils;

/**
 * Hibernate session factory, can be used with xml configuration
 * and annotation configuration.
 * 
 * This class is used in place of spring AnnotationSessionFactoryBean.
 *  
 * 
 * @author <a href="mailto:wayan.agustina@sigma.co.id">I Wayan Ari Agustina</a>
 *
 */

public class AdminHibernateSessionFactory extends AnnotationSessionFactoryBean {
	private Log log = LogFactory.getLog(AdminHibernateSessionFactory.class);
	
	private static final String RESOURCE_PATTERN = "/**/*.class";
	private String[] entityPackages;
	
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private TypeFilter[] entityTypeFilters = new TypeFilter[] {
			new AnnotationTypeFilter(Entity.class, false),
			new AnnotationTypeFilter(Embeddable.class, false),
			new AnnotationTypeFilter(MappedSuperclass.class, false),
			new AnnotationTypeFilter(org.hibernate.annotations.Entity.class, false)};
	
	/**
	 * Check whether any of the configured entity type filters matches
	 * the current class descriptor contained in the metadata reader.
	 */
	private boolean matchesFilter(MetadataReader reader, MetadataReaderFactory readerFactory) throws IOException {
		if (this.entityTypeFilters != null) {
			for (TypeFilter filter : this.entityTypeFilters) {
				if (filter.match(reader, readerFactory)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @return The registered entity packages
	 */
	public String[] getEntityPackages() {
		return entityPackages;
	}

	/**
	 * Set the packages of persistence classes, so we don't need to
	 * specify the persistence class explicitly.
	 * 
	 * @param entityPackages
	 */
	public void setEntityPackages(String[] entityPackages) {
		this.entityPackages = entityPackages;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		scannPackages();
		super.afterPropertiesSet();
	}
	
	/**
	 * Scan all packages for hibernate entity classes 
	 * and registering all the classes to the spring AnnotationSessionFactoryBean. 
	 */
	private void scannPackages() {
		if(null == entityPackages || entityPackages.length <=0){
			return;
		}
		List<Class<?>> entityList = new ArrayList<Class<?>>();
		
		for(String pkg : entityPackages) {
			try {
				String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
						ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
				Resource[] resources = this.resourcePatternResolver.getResources(pattern);
				MetadataReaderFactory readerFactory = new 
						CachingMetadataReaderFactory(this.resourcePatternResolver);
				
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader reader = readerFactory.getMetadataReader(resource);
						String className = reader.getClassMetadata().getClassName();
						if (matchesFilter(reader, readerFactory)) {
							
							try {
								Class<?> c = Class.forName(className);
								entityList.add(c);
							} catch (ClassNotFoundException e) {
								log.error("Error loading entity class resource. "+e.getMessage());
							}
						}
					}
				}
				
			} catch (IOException ioe) {
				log.error("Error loading entity class resource. "+ioe.getMessage());
			}
		}
		
		Class<?>[] entityClasses = new Class<?>[entityList.size()];
		entityList.toArray(entityClasses);
		setAnnotatedClasses(entityClasses);
	}
}
