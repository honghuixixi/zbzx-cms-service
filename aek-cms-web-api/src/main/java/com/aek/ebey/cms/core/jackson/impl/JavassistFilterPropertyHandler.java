package com.aek.ebey.cms.core.jackson.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.aek.ebey.cms.core.jackson.annotation.AllowProperty;
import com.aek.ebey.cms.core.jackson.annotation.IgnoreProperties;
import com.aek.ebey.cms.core.jackson.annotation.IgnoreProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.databind.ObjectMapper;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

//@IgnoreProperty(pojo = YxUserRoleRelation.class, name = { "id", "yxUser" })

/**
 * 使用代理来创建jackson的MixInAnnotation注解接口<br>
 * 如果使用本实现方法，一定要配置在web.xml中配置过滤器WebContextFilter，否则无法输出json到客户端
 *
 * @author 瑛琪 <a href="http://xiongyingqi.com">xiongyingqi.com</a>
 * @version 2013-10-25 下午2:31:21
 */
public class JavassistFilterPropertyHandler implements FilterPropertyHandler {

	public static final Logger logger = LoggerFactory.getLogger(JavassistFilterPropertyHandler.class);
	/**
	 * 注解的方法对应生成的代理类映射表
	 */
	private static Map<Method, Map<Class<?>, Class<?>>> proxyMethodMap = new HashMap<Method, Map<Class<?>, Class<?>>>();

	/**
	 * String数组的hashCode与生成的对应的代理类的映射表
	 */
	private static Map<Integer, Class<?>> proxyMixInAnnotationMap = new HashMap<Integer, Class<?>>();

	private static String[] globalIgnoreProperties = new String[] { "hibernateLazyInitializer", "handler" };

	/**
	 * 如果是标注的SpringMVC中的Controller方法，则应判断是否注解了@ResponseBody
	 */
	@SuppressWarnings("unused")
	private boolean isResponseBodyAnnotation;
	/**
	 * 创建代理接口的唯一值索引
	 */
	private static int proxyIndex;

	public JavassistFilterPropertyHandler() {
	}

	public JavassistFilterPropertyHandler(String[] globalIgnoreProperties) {
		JavassistFilterPropertyHandler.globalIgnoreProperties = globalIgnoreProperties;
	}

	/**
	 * @param isResponseBodyAnnotation
	 *            如果是标注的SpringMVC中的Controller方法，则应判断是否注解了@ResponseBody
	 */
	public JavassistFilterPropertyHandler(boolean isResponseBodyAnnotation) {
		this.isResponseBodyAnnotation = isResponseBodyAnnotation;
	}

	/**
	 * <br>
	 * 2013-10-28 上午11:11:24
	 *
	 * @param collection
	 * @param names
	 * @return
	 */
	private Collection<String> checkAndPutToCollection(Collection<String> collection, String[] names) {
		if (collection == null) {
			collection = new HashSet<String>();
		}
		Collections.addAll(collection, names);
		return collection;
	}

	private Collection<String> putGlobalIgnoreProperties(Collection<String> collection) {
		if (globalIgnoreProperties != null) {
			if (collection == null) {
				collection = new HashSet<String>();
			}
			for (int i = 0; i < globalIgnoreProperties.length; i++) {
				String name = globalIgnoreProperties[i];
				collection.add(name);
			}
		}
		return collection;
	}

	/**
	 * 处理IgnoreProperties注解 <br>
	 * 2013-10-30 下午6:15:41
	 *
	 * @param properties
	 * @param pojoAndNamesMap
	 */
	private void processIgnorePropertiesAnnotation(IgnoreProperties properties,
			Map<Class<?>, Collection<String>> pojoAndNamesMap) {
		IgnoreProperty[] values = properties.value();

		AllowProperty[] allowProperties = properties.allow();

		if (allowProperties != null) {
			for (AllowProperty allowProperty : allowProperties) {
				processAllowPropertyAnnotation(allowProperty, pojoAndNamesMap);
			}
		}

		if (values != null) {
			for (IgnoreProperty property : values) {
				processIgnorePropertyAnnotation(property, pojoAndNamesMap);
			}
		}

	}

	/**
	 * 处理IgnoreProperty注解 <br>
	 * 2013-10-30 下午6:16:08
	 *
	 * @param property
	 * @param pojoAndNamesMap
	 */
	private void processIgnorePropertyAnnotation(IgnoreProperty property,
			Map<Class<?>, Collection<String>> pojoAndNamesMap) {
		String[] names = property.name();
		Class<?> pojoClass = property.pojo();
		// Class<?> proxyAnnotationInterface = createMixInAnnotation(names);//
		// 根据注解创建代理接口

		Collection<String> nameCollection = pojoAndNamesMap.get(pojoClass);
		nameCollection = checkAndPutToCollection(nameCollection, names);
		pojoAndNamesMap.put(pojoClass, nameCollection);
	}

	private Collection<String> getAllowNameCollection(Class<?> clazz, Object view) {

		Collection<String> fieldNames = null;
		Field[] fields = clazz.getDeclaredFields();
		int length = fields.length;
		if (fieldNames == null)
			fieldNames = new ArrayList<String>();
		for (int i = 0; i < length; i++) {
			Field field = fields[i];
			if (field.getAnnotation(JsonView.class) != null) {
				JsonView jsonView = field.getAnnotation(JsonView.class);
				Object[] o = (Object[]) view;
				if (o.length > 0 && Arrays.asList(jsonView.value()).contains(o[0]))
					fieldNames.add(field.getName());
			}
		}

		Method[] methods = clazz.getDeclaredMethods();
		length = methods.length;
		for (int i = 0; i < length; i++) {
			if (methods[i].getAnnotation(JsonView.class) != null) {
				JsonView jsonView = methods[i].getAnnotation(JsonView.class);
				Object[] o = (Object[]) view;
				if (o.length > 0 && Arrays.asList(jsonView.value()).contains(o[0]))
					fieldNames.add(EntityHelper.getPropertieFromGetter(methods[i].getName()));
			}
		}

		if (clazz.getSuperclass() != null)
			fieldNames.addAll(getAllowNameCollection(clazz.getSuperclass(), view));

		return fieldNames;
	}

	/**
	 * 处理AllowProperty注解 <br>
	 * 2013-10-30 下午6:16:08
	 *
	 * @param property
	 * @param pojoAndNamesMap
	 */
	private void processAllowPropertyAnnotation(AllowProperty property,
			Map<Class<?>, Collection<String>> pojoAndNamesMap) {
		String[] allowNames = property.name();
		Class<?> pojoClass = property.pojo();

		Collection<String> ignoreProperties = EntityHelper.getUnstaticClassFieldNameCollection(pojoClass);

		Collection<String> allowNameCollection = new ArrayList<String>();
		Collections.addAll(allowNameCollection, allowNames);

		if (property.view() != null)
			allowNameCollection.addAll(getAllowNameCollection(pojoClass, property.view()));

		Collection<String> nameCollection = pojoAndNamesMap.get(pojoClass);
		if (nameCollection != null) {
			nameCollection.removeAll(allowNameCollection);
		} else {
			ignoreProperties.removeAll(allowNameCollection);
			nameCollection = ignoreProperties;
		}
		pojoAndNamesMap.put(pojoClass, nameCollection);
	}

	/**
	 * 根据方法获取过滤映射表 <br>
	 * 2013-10-25 下午2:47:34
	 *
	 * @param method
	 *            注解了 @IgnoreProperties 或 @IgnoreProperty 的方法（所在的类）
	 * @return Map<Class<?>, Collection<Class<?>>> pojo与其属性的映射表
	 */
	public Map<Class<?>, Class<?>> getProxyMixInAnnotation(Method method) {
		// if (isResponseBodyAnnotation &&
		// !method.isAnnotationPresent(ResponseBody.class)) {
		// return null;
		// }
		Map<Class<?>, Class<?>> map = proxyMethodMap.get(method);// 从缓存中查找是否存在

		if (map != null && map.entrySet().size() > 0) {// 如果已经读取该方法的注解信息，则从缓存中读取
			return map;
		} else {
			map = new HashMap<Class<?>, Class<?>>();
		}

		Class<?> clazzOfMethodIn = method.getDeclaringClass();// 方法所在的class

		Map<Class<?>, Collection<String>> pojoAndNamesMap = new HashMap<Class<?>, Collection<String>>();

		IgnoreProperties classIgnoreProperties = clazzOfMethodIn.getAnnotation(IgnoreProperties.class);
		IgnoreProperty classIgnoreProperty = clazzOfMethodIn.getAnnotation(IgnoreProperty.class);
		AllowProperty classAllowProperty = clazzOfMethodIn.getAnnotation(AllowProperty.class);

		IgnoreProperties ignoreProperties = method.getAnnotation(IgnoreProperties.class);
		IgnoreProperty ignoreProperty = method.getAnnotation(IgnoreProperty.class);
		AllowProperty allowProperty = method.getAnnotation(AllowProperty.class);

		if (allowProperty != null) {// 方法上的AllowProperty注解
			processAllowPropertyAnnotation(allowProperty, pojoAndNamesMap);
		}
		if (classAllowProperty != null) {
			processAllowPropertyAnnotation(classAllowProperty, pojoAndNamesMap);
		}

		if (classIgnoreProperties != null) {// 类上的IgnoreProperties注解
			processIgnorePropertiesAnnotation(classIgnoreProperties, pojoAndNamesMap);
		}
		if (classIgnoreProperty != null) {// 类上的IgnoreProperty注解
			processIgnorePropertyAnnotation(classIgnoreProperty, pojoAndNamesMap);
		}

		if (ignoreProperties != null) {// 方法上的IgnoreProperties注解
			processIgnorePropertiesAnnotation(ignoreProperties, pojoAndNamesMap);
		}
		if (ignoreProperty != null) {// 方法上的IgnoreProperties注解
			processIgnorePropertyAnnotation(ignoreProperty, pojoAndNamesMap);
		}

		Set<Entry<Class<?>, Collection<String>>> entries = pojoAndNamesMap.entrySet();

		for (Iterator<Entry<Class<?>, Collection<String>>> iterator = entries.iterator(); iterator.hasNext();) {
			Entry<Class<?>, Collection<String>> entry = (Entry<Class<?>, Collection<String>>) iterator.next();
			Collection<String> nameCollection = entry.getValue();
			nameCollection = putGlobalIgnoreProperties(nameCollection);// 将全局过滤字段放入集合内
			String[] names = nameCollection.toArray(new String[] {});
			// EntityHelper.print(entry.getKey());
			// for (int i = 0; i < names.length; i++) {
			// String name = names[i];
			// EntityHelper.print(name);
			// }
			Class<?> clazz = createMixInAnnotation(names);

			map.put(entry.getKey(), clazz);
		}

		proxyMethodMap.put(method, map);
		return map;
	}

	/**
	 * 创建jackson的代理注解接口类 <br>
	 * 2013-10-25 上午11:59:50
	 *
	 * @param names
	 *            要生成的字段
	 * @return 代理接口类
	 */
	private Class<?> createMixInAnnotation(String[] names) {
		Class<?> clazz = null;
		clazz = proxyMixInAnnotationMap.get(StringHelper.hashCodeOfStringArray(names));
		if (clazz != null) {
			return clazz;
		}

		ClassPool pool = ClassPool.getDefault();

		// 创建代理接口
		CtClass cc = pool.makeInterface("ProxyMixInAnnotation" + System.currentTimeMillis() + proxyIndex++);

		ClassFile ccFile = cc.getClassFile();
		ConstPool constpool = ccFile.getConstPool();

		// create the annotation
		AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
		// 创建JsonIgnoreProperties注解
		Annotation jsonIgnorePropertiesAnnotation = new Annotation(JsonIgnoreProperties.class.getName(), constpool);

		BooleanMemberValue ignoreUnknownMemberValue = new BooleanMemberValue(false, constpool);

		ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constpool);// value的数组成员

		Collection<MemberValue> memberValues = new HashSet<MemberValue>();
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			StringMemberValue memberValue = new StringMemberValue(constpool);// 将name值设入注解内
			memberValue.setValue(name);
			memberValues.add(memberValue);
		}
		arrayMemberValue.setValue(memberValues.toArray(new MemberValue[] {}));

		jsonIgnorePropertiesAnnotation.addMemberValue("value", arrayMemberValue);
		jsonIgnorePropertiesAnnotation.addMemberValue("ignoreUnknown", ignoreUnknownMemberValue);

		attr.addAnnotation(jsonIgnorePropertiesAnnotation);
		ccFile.addAttribute(attr);

		// generate the class
		try {
			clazz = cc.toClass();

			proxyMixInAnnotationMap.put(StringHelper.hashCodeOfStringArray(names), clazz);
			// JsonIgnoreProperties ignoreProperties = (JsonIgnoreProperties)
			// clazz
			// .getAnnotation(JsonIgnoreProperties.class);

			// EntityHelper.print(ignoreProperties);
			//
			// EntityHelper.print(clazz);

			// try {
			// Object instance = clazz.newInstance();
			// EntityHelper.print(instance);
			//
			// } catch (InstantiationException e) {
			// e.printStackTrace();
			// } catch (IllegalAccessException e) {
			// e.printStackTrace();
			// }
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}

		// right
		// mthd.getMethodInfo().addAttribute(attr);

		return clazz;

	}

	@Override
	public Object filterProperties(Method method, Object object) {

		Map<Class<?>, Class<?>> map = getProxyMixInAnnotation(method);
		if (map == null || map.entrySet().size() == 0) {// 如果该方法上没有注解，则返回原始对象
			return object;
		}

		ThreadJacksonMixInHolder.addMixIns(getEntries(map));

		return object;
	}

	public Set<Entry<Class<?>, Class<?>>> getEntries(Map<Class<?>, Class<?>> map) {
		Set<Entry<Class<?>, Class<?>>> entries = map.entrySet();
		return entries;
	}

	/**
	 * 根据指定的过滤表创建jackson对象 <br>
	 * 2013-10-25 下午2:46:43
	 *
	 * @param map
	 *            过滤表
	 * @return ObjectMapper
	 */
	private ObjectMapper createObjectMapper(Map<Class<?>, Class<?>> map) {
		ObjectMapper mapper = new ObjectMapper();
		Set<Entry<Class<?>, Class<?>>> entries = map.entrySet();
		for (Iterator<Entry<Class<?>, Class<?>>> iterator = entries.iterator(); iterator.hasNext();) {
			Entry<Class<?>, Class<?>> entry = iterator.next();
			mapper.addMixIn(entry.getKey(), entry.getValue());
		}
		return mapper;
	}

	/**
	 * 根据方法上的注解生成objectMapper
	 *
	 * @param method
	 * @return
	 */
	public ObjectMapper createObjectMapper(Method method) {
		return createObjectMapper(getProxyMixInAnnotation(method));
	}

	// /**
	// * 将结果输出到response <br>
	// * 2013-10-25 下午2:28:40
	// *
	// * @param objectMapper
	// * @param response
	// * @param object
	// */
	// private void writeJson(ObjectMapper objectMapper, HttpServletResponse
	// response, Object object) {
	// response.setContentType("application/json");
	//
	// JsonEncoding encoding = getJsonEncoding(response.getCharacterEncoding());
	// JsonGenerator jsonGenerator = null;
	// try {
	// jsonGenerator = objectMapper.getJsonFactory().createJsonGenerator(
	// response.getOutputStream(), encoding);
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	//
	// // A workaround for JsonGenerators not applying serialization features
	// // https://github.com/FasterXML/jackson-databind/issues/12
	// if (objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
	// jsonGenerator.useDefaultPrettyPrinter();
	// }
	//
	// try {
	// objectMapper.writeValue(jsonGenerator, object);
	// } catch (JsonProcessingException ex) {
	// LOGGER.error(ex);
	//
	// throw new HttpMessageNotWritableException("Could not write JSON: " +
	// ex.getMessage(),
	// ex);
	// } catch (IOException e) {
	// LOGGER.error(e);
	// // e.printStackTrace();
	// }
	//
	// }

	/**
	 * Determine the JSON encoding to use for the given content type.
	 *
	 * @param contentType
	 *            the media type as requested by the caller
	 * @return the JSON encoding to use (never {@code null})
	 */
	@SuppressWarnings("deprecation")
	protected JsonEncoding getJsonEncoding(MediaType contentType) {
		if (contentType != null && contentType.getCharSet() != null) {
			Charset charset = contentType.getCharSet();
			for (JsonEncoding encoding : JsonEncoding.values()) {
				if (charset.name().equals(encoding.getJavaName())) {
					return encoding;
				}
			}
		}
		return JsonEncoding.UTF8;
	}

}
