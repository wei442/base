package com.cloud.provider.redis.vo;

import java.io.Serializable;

/**
 *
 * WithScoresVo
 * @author wei.yong
 */
public class WithScoresVo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String element;

	private Double score;

	public String getElement() {
		return this.element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "WithScoresVo [element=" + element + ", score=" + score + "]";
	}

}