package it.betacom.ProgettoBiblioteca.service;



import java.lang.reflect.ParameterizedType;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;



// Classe utilizzata per semplificare operazioni che hanno a che fare con "Reflection"
public class ReflectionClassHandler {

	// Utilizzate per mappare, rispettivamente, classi java a tipi di
	// dato SQL e classi wrapper di java alle loro primitive
	public static final Map<Class<?>, Integer> sqlTypeMap = createSqlTypesMap();
	public static final Map<Class<?>, Class<?>> javaTypeMap = createJavaTypesMap();


	// Crea la "Map" per i tipi di dato di SQL.
	 // In Java 8 non è possibile creare una "Map" a partire da
	 // un insieme di valori, quindi tocca fare così.
	 // Al momento c'è un solo tipo perchè è l'unico che mi
	 // serviva.
	private static Map<Class<?>, Integer> createSqlTypesMap() {
		Map<Class<?>, Integer> map = new HashMap<>();
		map.put(Short.class, Types.SMALLINT);
		return map;
	}



	// Crea la mappa da classi wrapper a primitive.
	private static Map<Class<?>, Class<?>> createJavaTypesMap() {
		Map<Class<?>, Class<?>> map = new HashMap<>();
		map.put(Boolean.class, boolean.class);
		map.put(Byte.class, Byte.class);
		map.put(Character.class, char.class);
		map.put(Double.class, double.class);
		map.put(Float.class, float.class);
		map.put(Integer.class, int.class);
		map.put(Long.class, long.class);
		map.put(Short.class, short.class);
		return map;
	}



	public static int convertSqlType( Class<?> javaClass ) {
		return sqlTypeMap.get(javaClass);
	}



	public static Class<?> convertJavaType( Class<?> wrapperClass ) {
		return javaTypeMap.containsKey(wrapperClass) ?
				javaTypeMap.get(wrapperClass) :
				wrapperClass;
	}



	// Trasforma nomi di classe tipo "it.betacom. ... .Generi"
	// in "Generi"
	public static String stripClassName( Class<?> cls ) {
		String[] strs = cls.getTypeName().split("\\.");
		return strs[strs.length-1];
	}



	// Funzione che ritorna la classe del tipo parametrico di "cls"
	// ( es. classe DAO<Generi> -> classe Generi )
	public static Class<?> getTypeParameterClass( Class<?> cls ) {
		return ((Class<?>)
			( (ParameterizedType)
				cls.getGenericSuperclass()
			).getActualTypeArguments()[0]
		);
	}

	public static <T> Class<?> getTypeParameterClass( T obj ) {
		return getTypeParameterClass(obj.getClass());
	}

}
