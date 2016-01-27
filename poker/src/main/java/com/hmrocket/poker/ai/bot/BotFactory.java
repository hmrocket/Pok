package com.hmrocket.poker.ai.bot;

import com.hmrocket.poker.Player;
import com.hmrocket.poker.PokerTools;

import java.util.Random;

/**
 * Created by hmrocket on 03/11/2015.
 * Player Skills to learn:
 *	<ul>
 *	   <li>learn to detect bluff (lv. 5)</li>
 *	   <li>learned 4 poker movements ,Fold, Raise, Fold, Call (lv. 9)</li>
 *	   <li>Pre-Flop strategy (lv. 13) </li>
 *	   <li>Tight play (lv. 13) </li>
 *	   <li>Aggressive (lv. 18)</li>
 *	   <li>Loose play(lv. 18)</li>
 *	   <li>Loose Aggressive play style(lv. 22)</li>
 *	   <li>Strategy Check-RAISE (lv. 26)</li>
 *	   <li>Strategy STEAL (lv. 30)</li>
 *
 *	   <li>Kick ass poker bot at lv. 70</li>
 *	</ul>
 */
public final class BotFactory {

	/**
	 * Create a bot by just defining it's AI. level, the name, cash, bank will be generated
	 *
	 * @param botLevel bot level from 1 to 99
	 * @param minBet   minal bet that a player is required to have enter the game
	 * @return player
	 */
	public static Player generateBot(int botLevel, long minBet) {
		Random random = new Random();
		// depending on the level create bot with more money
		long minBuyIn = PokerTools.getMinBuyIn(minBet);
		long generatedCash = random.nextInt((int) (PokerTools.getMaxBuyIn(minBet) - minBuyIn)) + minBuyIn;
		// when the bot is busted it's busted under lv. 30
		long generatedBankBalance;
		if (botLevel < 30)
			generatedBankBalance = 0;
		else generatedBankBalance = random.nextInt(3) * generatedCash;

		String name = generateName(random, botLevel);
		//XXX if ("Samuel L. Jackson".equalsIgnoreCase(name)) {
			// Surprise motherfucker
			// Raise me one more god dam time motherf***er, I dare you! I DOUBLE DARE YOU!
		//}

		return pickBot(botLevel, name, generatedBankBalance, generatedCash);
	}

	/**
	 * Generate a random Poker bot name
	 *
	 * @param random random object (just to avoid creating a new one)
	 * @param level  level of the bot
	 * @return Random name of a bot
	 */
	private static String generateName(Random random, int level) {
		String[] names;
		switch (PokerRank.getRank(level)) {
			case FISH:
				names = new String[]{"Chicken", "Chickengoee",
						"Chickenda", "ChildChicken",
						"Fishamerve", "FishBillion", "Fishelecon", "Fisherbu", "FisherFish", "FisheryFish", "Aqua Bob",
						"Fishipul", "Fishringel", "FishShow", "Salmonal", "Salmondoge", "Salmon", "SalmonSalvo",
						"Chips",
						// Finding Nemo movie character names
						"Sushi", "Pumpkin", "Nemo", "Bubbles", "Peach", "Pearl", "Marlin", "Dory", "Bloat", "Deb"
				};
				break;
			case BEGINNER:
				names = new String[]{"Chief", "Charlie", "Burns", "Optimus", "Prime", "Cody", "Burns",
						"Heatwave", "Axel", "Frazier", "Bumblebee", "Jack", "Billy", "Shark", "Blastoff", "Chase",
						"Sawyer", "Storm", "Blades", "partner", "Walker", "Cleveland", "Boulder", "Sparkplug",
						"Blurr", "Salvage", "Medix", "Hoist", "Dani", "Graham", "Woodrow", "Boulder", "Servo", "Dither",
						"Trex", "Morocco", "Darth", "Vader", "Iceberg", "Draw"};
				break;
			case SHARK:
			case MASTER:
			default:
				names = new String[]{
						// real nicknames for great poker players
						// http://www.thetapoker.com/stans-lists-poker-player-nicknames-explained/
						"Action Dan", "Amarillo Slim", "Bald Eagle", "Bird Guts", "BoostedJ", "Chino", "Chip"
						, "Back to Back", "Clever Piggy", "Cowboy", "Crazy Horse", "The Croc", "Dandy", "Darkhorse"
						, "Devilfish", "Diamond Joe", "Downtown", "The Dragon", "The Duchess of Poker", "durrrr", "E-Dog"
						, "El Matador", "ElkY", "Eskimo", "The Finn", "Lady of Poker", "Flying Dutchman", "Fordman",
						"Fossilman", "Foucault", "Full Blown", "Furst Out", "Gentleman Jack", "Golden Boy", "Grand Old Man",
						"Grand Rapids Tom", "The Great Dane", "The Greek", "The Grinder", "Happy", "Hot Chips", "Iceman", "Iceman"
						, "Isildur1", "Isser", "Jennicide", "Jesus", "Joan", "Johnny World", "The Kid", "Kid Poker", "The King"
						, "The Knife", "Kwikfish", "Lady Maverick", "Luckbox", "Mad Genius", "The Magician", "The Master",
						"The Mathematician", "Miami", "Mister Cool", "Mixed Games", "Money", "The Monk", "The Mouth"
						, "Down Under", "Napoleon", "Noel", "Numbers", "Orient Express", "The Owl", "Poker Babe", "Poker Brat"
						, "PokerKat", "Prince of Poker", "The Professor", "Professor Backwards", "Puggy", "Raptor", "The Razor"
						, "Robin Hood", "The Rock", "Sailor", "Seiborg", "The Shadow", "Shaniac", "The Shark", "Sominex"
						, "Supernova", "Taxman", "Texas Dolly", "Tiger Woods of P.", "Tiltboy", "Treetop", "Unabomber", "Whatta Player"
						, "X-22"
				};
				break;
		}


		return names[random.nextInt(names.length)];
	}

	/**
	 * This is the brain of the game progress, here will decide what bot should challenge the player
	 * based on the playerLevel
	 *
	 * @param botLevel the bot progress level in the game
	 * @return Player (Bot) with a specific behavior passed on the player level
	 */
	private static Player pickBot(int botLevel, String name, long cash, long bank) {
		Random r;
		switch (botLevel) {
			case 1:
			case 2:
			case 3:
			case 4: // hand strength
				return new CallBot(name, cash, bank);
			case 5:
			case 6:
			case 7:
			case 8: // detect a bluff
				return new RaiseBot(name, cash, bank);
			case 9:
			case 10:
			case 11:
			case 12: // learned 4 poker movement
				return new RandBot(name, cash, bank);
			case 13: // Bonus level, apply what you learned and win.
				r = new Random();
				switch (r.nextInt(3)) {
					case 0:
						return new RaiseBot(name, cash, bank);
					case 1:
						return new CallBot(name, cash, bank);
					default:
					case 2:
						return new RandBot(name, cash, bank);
				}
			case 14: // Congratulation, we gonna add some artificial intelligence
			case 15:
			case 16:
			case 17: // Pre-Flop strategy, Tight Play
				r = new Random();
				switch (r.nextInt(3)) {
					case 0:
						return new SafeBot(name, cash, bank);
					case 1:
						return new SafeBotAi(name, cash, bank);
					case 2:
						return new SafeBotHoldemSecerts(name, cash, bank);
					case 3:
						return new SafeBotRakeBackPros(name, cash, bank);
					case 4:
					default:
						return new SafeBotM(name, cash, bank);
				}
			case 18: // loose play & aggressive play
			case 19:
			case 20:
			case 21:
			default: // from this point don't give a shit just send the same bot with different
				// aggressive and tight play
				return new Bot(name, botLevel, cash, bank);

		}
		// Level description: Extremely stupid bots, they will call you no matter what.
		// Objective: not get eliminate. survive and farm these bots till they get busted.
		// Level 2-4: same description, more bots and little richer.
		//Level 5 description: Extremely stupid bots, they will raise you no matter what.
		// Objective: learn when to raise. survive this mess to get to the next level.
		// bot learner 4 poker moves (R, C, F
		// (in online game, I call them babies with iPads)
	}

	/**
	 * Generate the exact same bot by specifying more parameter
	 *
	 * @param name     bot name
	 * @param botLevel Bot's level (needed to define what type of bot should be created
	 * @param cash     Cash of the bot
	 * @param bank     Bank account of the bot
	 * @return Player object
	 */
	public static Player createBot(int botLevel, String name, long cash, long bank) {
		return pickBot(botLevel, name, cash, bank);
	}


	public enum PokerRank {
		/**
		 * Level 1-13
		 */
		FISH,
		/**
		 * Level 14-29
		 */
		BEGINNER,
		/**
		 * LEVEL 30-59
		 */
		SHARK,
		/**
		 * LEVEL 60-99
		 */
		MASTER;

		public static PokerRank getRank(int playerLevel) {
			for (PokerRank rank : PokerRank.values()) {
				if (rank.isInRankRange(playerLevel))
					return rank;
			}
			return MASTER;
		}

		public boolean isInRankRange(int playerLevel) {
			return this.getLowerBound() <= playerLevel && this.getUpperBound() >= playerLevel;
		}

		public int getUpperBound() {
			switch (this) {
				case FISH:
					return 13;
				case BEGINNER:
					return 29;
				case SHARK:
					return 59;
				case MASTER:
					return 99;
				default:
					return 30;
			}
		}

		public int getLowerBound() {
			switch (this) {
				case FISH:
					return 1;
				case BEGINNER:
					return 14;
				case SHARK:
					return 30;
				case MASTER:
					return 60;
				default:
					return 30;
			}
		}
	}
}
