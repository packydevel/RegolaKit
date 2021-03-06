package org.regola.filter.criteria.criterion;

import java.util.Collection;

import org.regola.filter.criteria.Criterion;
import org.regola.filter.criteria.Criterion.Operator;

public class Restrictions {

	public static Criterion eq(String property, Object value) {
		return new Criterion(Operator.EQ, property, value);
	}

	public static Criterion ne(String property, Object value) {
		return new Criterion(Operator.NE, property, value);
	}

	public static Criterion gt(String property, Object value) {
		return new Criterion(Operator.GT, property, value);
	}

	public static Criterion lt(String property, Object value) {
		return new Criterion(Operator.LT, property, value);
	}

	public static Criterion ge(String property, Object value) {
		return new Criterion(Operator.GE, property, value);
	}

	public static Criterion le(String property, Object value) {
		return new Criterion(Operator.LE, property, value);
	}

	public static Criterion like(String property, String value) {
		return new Criterion(Operator.LIKE, property, value);
	}

	public static Criterion ilike(String property, String value) {
		return new Criterion(Operator.ILIKE, property, value);
	}

	public static Criterion in(String property, Collection<?> value) {
		return new Criterion(Operator.IN, property, value);
	}

	public static Criterion notIn(String property, Collection<?> value) {
		return new Criterion(Operator.NOTIN, property, value);
	}

	public static Criterion isNull(String property) {
		return new Criterion(Operator.ISNULL, property, null);
	}	
	
	public static Criterion isNotNull(String property) {
		return new Criterion(Operator.ISNOTNULL, property, null);
	}	

}
