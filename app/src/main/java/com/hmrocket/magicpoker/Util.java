package com.hmrocket.magicpoker;

import com.hmrocket.poker.card.Card;

import java.text.NumberFormat;

/**
 * @since 11/Nov/2015 - mhamed
 */
public final class Util {

	/**
	 * Format a number to short representation with max 2 digits
	 * Example: 100K or 1.12K
	 *
	 * @param amount value to be formatted
	 * @return a formatted short version of the value
	 */
	public static String formatNumberShort(long amount) {
		if (amount < 1000L) {
			return formatNumber(amount);
		}
		if (amount < 0xf4240) {
			StringBuilder stringbuilder = new StringBuilder();
			String s = formatNumber(amount / 1000D, 2);
			return stringbuilder.append(s).append("K").toString();
		} else {
			return (new StringBuilder()).append(formatNumber(amount / (double) 0xf4240, 2)).append("M").toString();
		}
	}

	/**
	 * Format a number with a max digit.
	 *
	 * @param amount   value to be formatted
	 * @param maxDigit max digit allowed in
	 * @return a formatted String of the value and possible digit with it depending on the amount
	 */
	private static String formatNumber(double amount, int maxDigit) {
		NumberFormat numberformat = NumberFormat.getCurrencyInstance();
		numberformat.setGroupingUsed(true);
		numberformat.setMaximumFractionDigits(maxDigit);
		return numberformat.format(amount);
	}

	/**
	 * Format a number
	 * Example: 100,000 or 1,000,000
	 *
	 * @param amount value to be formatted
	 * @return a formatted String of the value
	 */
	public static String formatNumber(long amount) {
		NumberFormat numberformat = NumberFormat.getCurrencyInstance();
		numberformat.setGroupingUsed(true);
		numberformat.setMaximumFractionDigits(0);
		return numberformat.format(amount);
	}

	public static int getCardImageId(Card card) {
		//TODO implement
		return R.mipmap.ic_launcher;
	}
}
