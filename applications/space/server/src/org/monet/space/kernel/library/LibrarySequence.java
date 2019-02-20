/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.kernel.library;

public class LibrarySequence {

	public static String fillWithZeros(Integer id, Integer length) {
		String result = String.valueOf(id);
		while (result.length() < length) result = "0" + result;
		return result;
	}

	public static String getCode(Integer id, Integer length) {
		Integer i1, i2, i3;
		String result;

		i1 = id;
		i2 = (int) (i1 / 1000);
		i3 = (int) (i2 / 1000);

		result = CODES[i1 % 1000] + CODES[i2 % 1000] + CODES[i3 % 1000];
		result = result.substring(0, 3) + result.substring(6, 9) + result.substring(3, 6) + result.substring(9, 12);

		if (length < 12) return result.substring(0, length);

		while (result.length() < length) result += "0";

		return result;
	}

	private static final String[] CODES = {"AB0A", "20HJ", "NMXC", "MC51", "3WZ4", "49DE", "QW91", "NM91", "2513", "6LA1", "4AB5", "NMAS", "JKJK", "0CD8", "9364", "2720", "8ER5", "4854", "1QW8", "4AS9",
		"53MX", "0WE1", "8WZ6", "8798", "8ER6", "6WE7", "2548", "25WE", "JK79", "3732", "1736", "7694", "24NM", "10A8", "HJ68", "38SD", "39RT", "MZ43", "8SD5", "0DE9",
		"9285", "BNRT", "GH26", "8CV6", "2AS6", "1692", "8653", "79CD", "0TY2", "3TY6", "7413", "70LA", "5193", "6380", "2819", "84QW", "5249", "0A43", "3VB9", "0A71",
		"2AB9", "50A6", "0A14", "2TY7", "46CD", "AB49", "2624", "68XC", "2873", "7WE2", "1BN8", "1MC7", "RT57", "5164", "6XC7", "6461", "7WE6", "39WE", "6831", "3BC6",
		"3910", "3938", "DF79", "0A69", "5MC8", "31LA", "WE42", "8193", "38AS", "4630", "QW50", "84NM", "7468", "6360", "82CV", "TYLA", "ER41", "XC71", "9ER0", "9JK2",
		"2768", "4371", "5RT2", "17FG", "FG29", "9651", "7538", "4LA7", "WZNM", "5SD0", "5AS9", "8328", "5CV7", "2979", "9DF5", "3940", "5982", "0YU8", "9386", "9841",
		"BN83", "8BC6", "5NM1", "6832", "WZMX", "8624", "7581", "31XC", "9868", "92BC", "WE36", "2652", "2HJ2", "AB80", "8525", "2492", "1MC1", "3247", "8VB9", "CDBC",
		"62GH", "6584", "4398", "QW21", "7154", "1AB1", "5381", "6810", "4RT7", "ER27", "3AS9", "94LA", "71GH", "3713", "6572", "4357", "6158", "0LA9", "5769", "93BN",
		"1985", "8354", "WEDF", "2QW7", "7YU9", "SD58", "2VB0", "8HJ4", "JKDE", "9GH0", "MZ47", "1695", "KL58", "2686", "9624", "AB52", "1DE7", "9TY0", "4ER2", "3636",
		"6843", "AB46", "5727", "73MC", "7MZ3", "1RT2", "1597", "BC53", "4948", "QWMX", "30A2", "7395", "7657", "85CV", "CD52", "47WZ", "2798", "6DF6", "1320", "0A51",
		"8272", "60QW", "ABBC", "WZ65", "4827", "2498", "9WZ4", "73CV", "2JK1", "QW76", "VBQW", "0ER6", "0TY4", "CDLA", "1TY6", "8584", "91MX", "6947", "3527", "2CD6",
		"MZAB", "6SD9", "6437", "36CV", "1483", "BC46", "BN37", "9715", "DE46", "3YU8", "5917", "1WE2", "HJ98", "1384", "1476", "9143", "0A29", "QW15", "DF69", "WE41",
		"FG61", "6264", "DF19", "1542", "LA61", "5482", "YU95", "76DF", "1387", "5KL1", "1415", "5YU3", "8DE6", "GHRT", "9HJ9", "FG63", "1VB1", "7DE6", "DE59", "3193",
		"BN70", "VBCV", "2MZ3", "40CD", "57DE", "3740", "8321", "5CD2", "7632", "82ER", "9315", "8621", "42HJ", "3MZ3", "7143", "BCYU", "HJDE", "LA69", "BNNM", "5NM8",
		"61QW", "MZ93", "0ANM", "VB63", "GH10", "WE25", "73DE", "1935", "41GH", "6282", "4851", "8142", "4MX0", "3583", "AB74", "50A4", "HJ10", "1435", "9750", "73MX",
		"5371", "1591", "3150", "0KL2", "MX76", "1958", "42NM", "3GH5", "10A6", "DE83", "0A76", "8419", "TY71", "62MC", "3581", "4641", "1ER1", "XC14", "WZ30", "MC74",
		"MC15", "QWMC", "5814", "8576", "4MX4", "MC73", "54CD", "7368", "5TY8", "HJ38", "WZJK", "6FG0", "1DF6", "FG72", "30GH", "HJAB", "460A", "25BN", "WZRT", "2517",
		"1919", "5425", "0JK3", "7250", "14XC", "57MX", "3683", "7637", "SD84", "1725", "RT83", "3749", "5157", "2486", "2163", "GH95", "83MC", "MX63", "8632", "3MZ6",
		"2830", "3MC4", "5481", "7392", "6QW2", "3840", "DE10", "7DE3", "SD53", "8FG0", "2639", "3XC8", "93DE", "MZ65", "2CV7", "5FG8", "7614", "RT25", "2725", "3693",
		"5165", "BN29", "4692", "7482", "58VB", "0A59", "HJ59", "60A6", "6824", "52BC", "0LA6", "CDGH", "79DF", "6AS1", "QWCD", "4DE8", "93SD", "37FG", "0CD9", "1MZ6",
		"39AB", "61BC", "6SD5", "QW39", "JK69", "5AS5", "DEAB", "4727", "2817", "LA42", "WZHJ", "9659", "80QW", "VB14", "14AS", "XC16", "GH52", "7621", "7517", "3259",
		"NM52", "CD50", "1543", "2136", "8CD8", "3GH4", "CVVB", "SDXC", "3639", "6VB8", "580A", "3659", "3RT4", "MZBC", "5CV0", "5294", "9GH4", "5765", "2YU5", "8AB0",
		"2NM9", "WEBN", "1AB5", "CV19", "7SD0", "4949", "620A", "4293", "5832", "8AB8", "1ER5", "4YU2", "3624", "98SD", "8DF9", "CVGH", "3696", "5283", "25DE", "3857",
		"85DF", "43NM", "CVWE", "43MQ", "7541", "3610", "4YU4", "7CV9", "BC83", "3AS3", "4WZ6", "MC49", "1WE1", "4LA5", "ABCV", "4CV9", "JK72", "6958", "1965", "9329",
		"910A", "9DE1", "59KL", "80A0", "3532", "3572", "2637", "CD26", "8153", "CD28", "CDJK", "4BN4", "TY29", "LA95", "8BN2", "10QW", "92FG", "8393", "9472", "8315",
		"63VB", "LA81", "7526", "RTHJ", "0AMX", "WZ58", "1416", "76NM", "9537", "3YU5", "1CV0", "7535", "4791", "7252", "63KL", "97FG", "WEYU", "1JK9", "8372", "ASDF",
		"SDCD", "6485", "50A0", "47XC", "GH91", "DE71", "9WE3", "9570", "4693", "NM57", "8750", "QWWZ", "9421", "80SD", "3582", "MX46", "LA93", "76ER", "4249", "7253",
		"61MC", "7492", "2541", "5950", "YU31", "8516", "GHJK", "4947", "JK17", "MX10", "4DF8", "40A4", "2AS7", "DF94", "98DF", "5360", "SD63", "7NM8", "0A64", "7LA6",
		"YUDF", "2921", "7939", "97AS", "1548", "7BN4", "9538", "3726", "9TY7", "FG86", "TY73", "TYAS", "3JK1", "10KL", "3VB2", "DE86", "FG80", "MX70", "38GH", "0A40",
		"MXDE", "9852", "CV94", "7984", "29BC", "AB13", "2WE7", "5AB7", "MX25", "1GH4", "1575", "5321", "DF83", "3285", "5AS7", "5914", "3HJ5", "6195", "GH94", "HJ96",
		"9682", "BC95", "26NM", "8HJ9", "13DE", "6ER5", "QW52", "WZAS", "KL20", "21FG", "20DE", "5243", "2951", "6350", "MC64", "WZMZ", "69SD", "63YU", "ER65", "7497",
		"5141", "2MX5", "2846", "87SD", "GH49", "FGBN", "AB17", "ER82", "6CD7", "1GH0", "9ER7", "9587", "70A4", "5FG9", "8317", "80A6", "AB61", "5NM3", "9CD6", "3170",
		"DEMX", "CVBC", "41RT", "TY35", "CDBN", "38DE", "17AB", "3741", "YUDE", "6413", "4NM9", "9637", "3596", "3172", "RT13", "2684", "TYMC", "52RT", "7516", "2GH7",
		"9471", "48CD", "9173", "YU64", "WERT", "4758", "6WE4", "JK61", "8519", "5762", "950A", "HJ48", "7295", "70HJ", "8361", "ER15", "1696", "0GH0", "38ER", "7161",
		"1631", "4SD4", "4MC7", "86MZ", "85BC", "81JK", "1962", "14SD", "0A26", "4310", "QW62", "5427", "8276", "1632", "2YU6", "97BC", "3240", "82QW", "7MZ5", "5WZ7",
		"3969", "7CV6", "KLTY", "WZ41", "93BC", "61MX", "4327", "9RT5", "1491", "6369", "75YU", "RT19", "93VB", "KL10", "GHMX", "6BC2", "3861", "VB31", "9141", "HJ25",
		"DF51", "BN28", "84MX", "8161", "HJ30", "CV60", "MXBC", "2831", "8586", "35BC", "83YU", "60ER", "70FG", "1430", "5140", "CV83", "2HJ5", "1385", "31KL", "CV24",
		"74HJ", "6976", "CDMZ", "6AB9", "QW57", "72AB", "27BN", "8439", "2828", "5261", "2493", "13RT", "8NM5", "4360", "9473", "0AWZ", "7484", "73WZ", "YU97", "1VB7",
		"2658", "MZ64", "5246", "2147", "3GH7", "4281", "6DF9", "GHQW", "XC80", "0LA7", "40ER", "8AS4", "3548", "6983", "7420", "2972", "2651", "3169", "HJ17", "7XC0",
		"74KL", "2524", "2540", "3258", "KL50", "AS79", "31NM", "17HJ", "1YU4", "VB98", "9463", "7MC8", "2XC8", "35DE", "8359", "ER74", "42CD", "CD95", "0A75", "JKNM",
		"JK91", "KL62", "AS10", "VB70", "TY21", "VBMX", "79YU", "54DF", "5LA6", "32CD", "95BN", "1WE6", "8595", "6CD8", "8754", "7LA8", "6930", "14CD", "2729", "1YU9",
		"RT98", "2FG8", "27BC", "3GH2", "8TY1", "DF64", "8351", "VB96", "MC79", "3525", "1524", "9520", "8RT2", "40HJ", "9432", "KLDF", "CD40", "JK97", "4174", "8326",
		"26SD", "NMMZ", "QW51", "91MC", "7649", "47MZ", "0HJ3", "AS71", "9391", "YU87", "2410", "43CD", "7RT7", "WE57", "0A91", "87GH", "MCWZ", "43MZ", "DF76", "MX64",
		"CVDE", "4257", "92JK", "5QW4", "9653", "MC43", "6258", "7RT0", "8BN1", "2463", "QW29", "4749", "9649", "BN17", "20TY", "75FG", "1516", "FGMZ", "2829", "6963",
		"3ER4", "3921", "2748", "2137", "79AS", "9279", "4958", "3CV5", "1437", "XC63", "6DE7", "0HJ9", "7243", "8MC1", "MC25", "3XC7", "3149", "1QW4", "4RT4", "AS39",
		"CV25", "1641", "97SD", "68HJ", "8RT0", "6573", "5BN9", "7DF8", "7DF5", "7130", "24JK", "4635", "7160", "FG21", "6576", "8275", "7GH8", "9150", "5153", "BN79",
		"9SD7", "9GH3", "3NM2", "1724", "BN73", "NMLA", "JK36", "2954", "2AS9", "6479", "26TY", "LA29", "YU84", "JKSD", "2529", "TY39", "7CD3", "41FG", "WZ63", "3BC4",
		"BCMZ", "2757", "5932", "86LA", "1926", "BN49", "ER46", "5253", "7370", "BN10", "3284", "2197", "9QW8", "MX71", "GH58", "0SD1", "7261", "BCSD", "DE65", "BNDE",
		"WZWZ", "3943", "81MX", "AB37", "ASAB", "RT63", "8585", "WZ10", "7BC8", "1953", "4697", "3872", "KL79", "6DE3", "6FG3", "6XC4", "62HJ", "28CV", "5XC4", "0QW6",
		"VB97", "9652", "9CD0", "51JK", "9DE3", "7691", "9281", "1979", "59DF", "58MC", "4976", "8NM9", "6268", "5217", "6396", "5DF0", "3SD8", "6YU5", "QW41", "4KL0"};

}
