package xyz.chandlerph.spider.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextExtractor {

	/** 行块大小. */
	private static Integer _block = 3;

	private final static String _titlePattern = "<title>(.*?)</title>";

	private final static Pattern _titleRegexPattern = Pattern.compile(_titlePattern,
			Pattern.CANON_EQ | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private String _title = "";

	private String _text = "";

	@SuppressWarnings("unused")
	private static void setBlock(Integer block) {
		_block = block;
	}

	public boolean isContentURL(String url) {
		int count = 0;
		for (int i = 0; i < url.length() - 1 && count < 3; i++) {
			if (url.charAt(i) == '/')
				count++;
		}

		return count > 2;
	}

	public boolean isLegalURL(String url) {
		if (url == null || url.length() == 0) {
			return false;
		}

		String regEx = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
				+ "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
				+ "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
				+ "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
				+ "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
				+ "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
				+ "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
				+ "[a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(url);
		System.out.println(matcher.matches());
		return matcher.matches();
	}

	public void extractHTML(String htmlText) {
		// System.out.println(htmlText);
		extractTitle(htmlText);
		htmlText = preProcess(htmlText);
		if (!isContentPage(htmlText)) {
			_text = "*推测您提供的网页为非主题型网页，目前暂不处理！:-)";
			return;
		}
		// System.out.println(htmlText);

		List<String> lines = Arrays.asList(htmlText.split("\n"));
		List<Integer> indexDistribution = lineBlockDistribute(lines);

		List<String> textList = new ArrayList<String>();
		List<Integer> textBeginList = new ArrayList<Integer>();
		List<Integer> textEndList = new ArrayList<Integer>();

		for (int i = 0; i < indexDistribution.size(); i++) {
			if (indexDistribution.get(i) > 0) {
				StringBuilder tmp = new StringBuilder();
				textBeginList.add(i);
				while (i < indexDistribution.size() && indexDistribution.get(i) > 0) {
					tmp.append(lines.get(i)).append("\n");
					i++;
				}
				textEndList.add(i);
				textList.add(tmp.toString());
			}
		}

		// 如果两块只差两个空行，并且两块包含文字均较多，则进行块合并，以弥补单纯抽取最大块的缺点
		for (int i = 1; i < textList.size(); i++) {
			if (textBeginList.get(i) == textEndList.get(i - 1) + 1
					&& textEndList.get(i) > textBeginList.get(i) + _block
					&& textList.get(i).replaceAll("\\s+", "").length() > 40) {
				if (textEndList.get(i - 1) == textBeginList.get(i - 1) + _block
						&& textList.get(i - 1).replaceAll("\\s+", "").length() < 40) {
					continue;
				}
				textList.set(i - 1, textList.get(i - 1) + textList.get(i));
				textEndList.set(i - 1, textEndList.get(i));

				textList.remove(i);
				textBeginList.remove(i);
				textEndList.remove(i);
				--i;
			}
		}

		String result = "";
		for (String text : textList) {
			// System.out.println("text:" + text + "\n" + text.replaceAll("\\s+",
			// "").length());
			if (text.replaceAll("\\s+", "").length() > result.replaceAll("\\s+",
					"").length())
				result = text;
		}

		// 最长块长度小于100，归为非主题型网页
		if (result.replaceAll("\\s+", "").length() < 100)
			_text = "*推测您提供的网页为非主题型网页，目前暂不处理！:-)";
		else
			_text = result;
	}

	private boolean isContentPage(String htmlText) {
		int count = 0;
		for (int i = 0; i < htmlText.length() && count < 5; i++) {
			if (htmlText.charAt(i) == '，' || htmlText.charAt(i) == '。')
				count++;
		}

		return count >= 5;
	}

	private void extractTitle(String htmlText) {
		Matcher m1 = _titleRegexPattern.matcher(htmlText);

		if (m1.find()) {
			_title = replaceSpecialChar(m1.group(1));
		}
		_title = _title.replaceAll("\n+", "");
	}

	public String getTitle() {
		return _title;
	}

	public String getText() {
		return _text;
	}

	private List<Integer> lineBlockDistribute(List<String> lines) {
		List<Integer> indexDistribution = new ArrayList<Integer>();

		for (int i = 0; i < lines.size(); i++) {
			indexDistribution.add(lines.get(i).replaceAll("\\s+", "").length());
		}
		// 删除上下存在两个空行的文字行
		for (int i = 0; i + 4 < lines.size(); i++) {
			if (indexDistribution.get(i) == 0 && indexDistribution.get(i + 1) == 0
					&& indexDistribution.get(i + 2) > 0
					&& indexDistribution.get(i + 2) < 40
					&& indexDistribution.get(i + 3) == 0
					&& indexDistribution.get(i + 4) == 0) {
				// System.out.println("line:" + lines.get(i+2));
				lines.set(i + 2, "");
				indexDistribution.set(i + 2, 0);
				i += 3;
			}
		}

		for (int i = 0; i < lines.size() - _block; i++) {
			int wordsNum = indexDistribution.get(i);
			for (int j = i + 1; j < i + _block && j < lines.size(); j++) {
				wordsNum += indexDistribution.get(j);
			}
			indexDistribution.set(i, wordsNum);
		}

		return indexDistribution;
	}

	public String preProcess(String htmlText) {
		// DTD
		htmlText = htmlText.replaceAll("(?is)<!DOCTYPE.*?>", "");
		// html comment
		htmlText = htmlText.replaceAll("(?is)<!--.*?-->", "");
		// js
		htmlText = htmlText.replaceAll("(?is)<script.*?>.*?</script>", "");
		// css
		htmlText = htmlText.replaceAll("(?is)<style.*?>.*?</style>", "");
		// html
		htmlText = htmlText.replaceAll("(?is)<.*?>", "");

		return replaceSpecialChar(htmlText);
	}

	public String replaceSpecialChar(String content) {
		String text = content.replaceAll("&quot;", "\"");
		text = text.replaceAll("&ldquo;", "“");
		text = text.replaceAll("&rdquo;", "”");
		text = text.replaceAll("&middot;", "·");
		text = text.replaceAll("&#8231;", "·");
		text = text.replaceAll("&#8212;", "——");
		text = text.replaceAll("&#28635;", "濛");
		text = text.replaceAll("&hellip;", "…");
		text = text.replaceAll("&#23301;", "嬅");
		text = text.replaceAll("&#27043;", "榣");
		text = text.replaceAll("&#8226;", "·");
		text = text.replaceAll("&#40;", "(");
		text = text.replaceAll("&#41;", ")");
		text = text.replaceAll("&#183;", "·");
		text = text.replaceAll("&amp;", "&");
		text = text.replaceAll("&bull;", "·");
		text = text.replaceAll("&lt;", "<");
		text = text.replaceAll("&#60;", "<");
		text = text.replaceAll("&gt;", ">");
		text = text.replaceAll("&#62;", ">");
		text = text.replaceAll("&nbsp;", " ");
		text = text.replaceAll("&#160;", " ");
		text = text.replaceAll("&tilde;", "~");
		text = text.replaceAll("&mdash;", "—");
		text = text.replaceAll("&copy;", "@");
		text = text.replaceAll("&#169;", "@");
		text = text.replaceAll("♂", "");
		text = text.replaceAll("\r\n|\r", "\n");

		return text;
	}

}
