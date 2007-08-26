package org.regola;

import java.util.Arrays;
import java.util.Collection;

/**
 * Criterio di filtro
 * <p>
 * Rappresenta il tipo di criterio (operatore), la propriet&agrave; interessata
 * e il valore di filtraggio.
 * 
 */
public class Criterion {

	private Operator operator;
	private String property;
	private Object value;

	public Criterion(Operator operator, String property, Object value) {
		if (operator == null) {
			throw new RuntimeException(
					"Indicare l'operatore nella creazione di un nuovo criterio");
		}
		this.operator = operator;
		if (property == null) {
			throw new RuntimeException(
					"Indicare la proprietà nella creazione di un nuovo criterio");
		}
		this.property = property;
		this.value = value;
	}

	/**
	 * Tipo di criterio
	 */
	public static enum Operator {
		EQ("=") {
			void dispatch(Builder builder, String property, Object value) {
				builder.addEquals(property, value);
			}
		},
		NE("<>") {
			void dispatch(Builder builder, String property, Object value) {
				builder.addNotEquals(property, value);
			}
		},
		GT(">") {
			void dispatch(Builder builder, String property, Object value) {
				builder.addGreaterThan(property, value);
			}
		},
		LT("<") {
			void dispatch(Builder builder, String property, Object value) {
				builder.addLessThan(property, value);
			}
		},
		GE(">=") {
			void dispatch(Builder builder, String property, Object value) {
				builder.addGreaterEquals(property, value);
			}
		},
		LE("<=") {
			void dispatch(Builder builder, String property, Object value) {
				builder.addLessEquals(property, value);
			}
		},
		LIKE("LIKE") {
			void dispatch(Builder builder, String property, Object value) {
				if (!(value instanceof String)) {
					throw new RuntimeException(
							"L'operatore LIKE supporta solo valori di tipo String");
				}
				builder.addLike(property, (String) value);
			}
		},
		ILIKE("ILIKE") {
			void dispatch(Builder builder, String property, Object value) {
				if (!(value instanceof String)) {
					throw new RuntimeException(
							"L'operatore ILIKE supporta solo valori di tipo String");
				}
				builder.addIlike(property, (String) value);
			}
		},
		IN("IN") {
			void dispatch(Builder builder, String property, Object value) {
				if (value instanceof Collection) {
					builder.addIn(property, (Collection<?>) value);
				}
				if (value != null && value.getClass().isArray()) {
					builder.addIn(property, Arrays.asList(value));
				}
				throw new RuntimeException(
						"L'operatore IN supporta solo valori di tipo Collection o Array");

			}
		};

		private String token;

		Operator(String token) {
			this.token = token;
		}

		public String toString() {
			return token;
		}

		abstract void dispatch(Builder builder, String property, Object value);
	}

	public interface Builder {
		public void addEquals(String property, Object value);

		public void addNotEquals(String property, Object value);

		public void addGreaterThan(String property, Object value);

		public void addLessThan(String property, Object value);

		public void addGreaterEquals(String property, Object value);

		public void addLessEquals(String property, Object value);

		public void addLike(String property, String value);

		public void addIlike(String property, String value);

		public void addIn(String property, Collection<?> value);
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Object getValue() {
		return value;
	}

	public void setValues(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getProperty()).append(" ").append(getOperator()).append(" ")
				.append("'").append(getValue()).append("'");
		return sb.toString();
	}
}
