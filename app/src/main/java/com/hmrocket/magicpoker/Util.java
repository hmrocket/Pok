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

	public static int getCardImageId(Card card) {
		switch (card.getRank()) {
			case ACE:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.da;
					case CLUBS:
						return R.mipmap.ca;
					case HEARTS:
						return R.mipmap.ha;
					case SPADES:
						return R.mipmap.sa;
				}
			case KING:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.dk;
					case CLUBS:
						return R.mipmap.ck;
					case HEARTS:
						return R.mipmap.hk;
					case SPADES:
						return R.mipmap.sk;
				}
			case QUEEN:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.dq;
					case CLUBS:
						return R.mipmap.cq;
					case HEARTS:
						return R.mipmap.hq;
					case SPADES:
						return R.mipmap.sq;
				}
			case JACK:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.dj;
					case CLUBS:
						return R.mipmap.cj;
					case HEARTS:
						return R.mipmap.hj;
					case SPADES:
						return R.mipmap.sj;
				}
			case TEN:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.d10;
					case CLUBS:
						return R.mipmap.c10;
					case HEARTS:
						return R.mipmap.h10;
					case SPADES:
						return R.mipmap.s10;
				}
			case NINE:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.d9;
					case CLUBS:
						return R.mipmap.c9;
					case HEARTS:
						return R.mipmap.h9;
					case SPADES:
						return R.mipmap.s9;
				}
			case EIGHT:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.d8;
					case CLUBS:
						return R.mipmap.c8;
					case HEARTS:
						return R.mipmap.h8;
					case SPADES:
						return R.mipmap.s8;
				}
			case SEVEN:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.d7;
					case CLUBS:
						return R.mipmap.c7;
					case HEARTS:
						return R.mipmap.h7;
					case SPADES:
						return R.mipmap.s7;
				}
			case SIX:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.d6;
					case CLUBS:
						return R.mipmap.c6;
					case HEARTS:
						return R.mipmap.h6;
					case SPADES:
						return R.mipmap.s6;
				}
			case FIVE:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.d5;
					case CLUBS:
						return R.mipmap.c5;
					case HEARTS:
						return R.mipmap.h5;
					case SPADES:
						return R.mipmap.s5;
				}
			case FOUR:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.d4;
					case CLUBS:
						return R.mipmap.c4;
					case HEARTS:
						return R.mipmap.h4;
					case SPADES:
						return R.mipmap.s4;
				}
			case THREE:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.d3;
					case CLUBS:
						return R.mipmap.c3;
					case HEARTS:
						return R.mipmap.h3;
					case SPADES:
						return R.mipmap.s3;
				}
			case TWO:
				switch (card.getSuit()) {
					case DIAMONDS:
						return R.mipmap.d2;
					case CLUBS:
						return R.mipmap.c2;
					case HEARTS:
						return R.mipmap.h2;
					case SPADES:
						return R.mipmap.s2;
				}
		}
		return R.mipmap.backcover;
	}
}
