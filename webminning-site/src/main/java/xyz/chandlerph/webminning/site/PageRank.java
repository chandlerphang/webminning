package xyz.chandlerph.webminning.site;

/**
 * 
 * @author Chandler Phang
 */
public class PageRank {

	public static void main(String[] args) {
		// 定义阻尼系数
		double damp = 0.85;

		/* 
		 * 测试数据，表示四个网页相互之间的有向链接矩阵 
		 * 用矩阵形式表示 
		 *    A  B  C  D  E
		 * A  0  1  1  1  1 
		 * B  1  0  1  0  1
		 * C  0  0  0  1  1
		 * D  0  0  0  1  0
		 * E  0  0  0  0  0
		 */  
		int[][] link = { 
			{ 0, 1, 1, 1, 1 }, 
			{ 1, 0, 1, 0, 1 }, 
			{ 0, 0, 0, 1, 1 }, 
			{ 0, 0, 0, 1, 0 },
			{ 0, 0, 0, 0, 0 }
		};

		// 各点的总链出数量
		int[] linkOut = new int[link.length];
		for (int i = 0; i < link.length; i++) {
			for (int j = 0; j < link.length; j++) {
				linkOut[i] += link[i][j];
			}
		}

		// 初始pr值
		double[] pr = new double[link.length];
		for (int i = 0; i < link.length; i++) {
			pr[i] = 1.0 / link.length;
		}
		
		// 我们进行20次迭代计算pagerank的值
		for (int i = 0; i < 10; i++) {
			pr = pagerank(link, damp, linkOut, pr);
		}

		for (int i = 0; i < link.length; i++) {
			System.out.println("PR(" + i + ") = " + pr[i]);
		}

	}

	public static double[] pagerank(int[][] link, double damp, int[] linkout, double[] last) {
		double[] pr = new double[last.length];
		for (int i = 0; i < last.length; i++) {
			double num = 0;
			for (int j = 0; j < last.length; j++) {
				if (link[j][i] != 0)
					num = num + last[j] / linkout[j];
			}
			// pagerank的计算公式
			pr[i] = damp / link.length + (1 - damp) * num;
		}
		return pr;
	}
}
