package src;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class DirFile {

	static private ArrayList<File> files;

	/**
	 * dir以下のディレクトリ以外のFileインスタンスをすべて取得する。
	 * @param dir - ディレクトリを示すこのFileインスタンス以下が検索対象となる。
	 * @return dir以下のディレクトリ以外のFileインスタンスのArrayList
	 */
	static public ArrayList<File> getAllFiles(File dir) {

		DirFile.files = new ArrayList<File>();
		readFolder(dir);
		return files;

	}

	/**
	 * dirが示すディレクトリ以下のすべてのファイルのうち正規表現にマッチするファイルを取得する。
	 * @param dir - ディレクトリを示すこのFileインスタンス以下が検索対象となる。
	 * @param regex - 検索するファイルの正規表現。Patternクラスの記述方法に従う。
	 * @return 正規表現にマッチしたファイルのFileインスタンスをArrayListで返す。
	 *         マッチするファイルが存在しない場合も要素が空のArrayListを返す。
	 */
	static public ArrayList<File> getAllFiles(File dir, String regex) {

		Pattern p = Pattern.compile(regex);

		ArrayList<File> fileList = getAllFiles(dir);

		int index = 0;
		File file;
		while(index < fileList.size()) {
			file = fileList.get(index);
			if(!p.matcher(file.getName()).matches()) {
				fileList.remove(index);
				index--;
			}
			index++;
		}
		return files;

	}

	/**
	 * ディレクトリdir以下のすべてのファイルのFileインスタンスを再帰的に取得してthis.filesに格納する。
	 * つまりthis.filesにはディレクトリを示すFileインスタンスは含まれない。
	 * @param dir - ディレクトリを示すこのFileインスタンス以下が検索対象となる。
	 */
	private static void readFolder(File dir) {

		File[] files = dir.listFiles();
		if (files == null)
			return;
		for (File file : files) {
			if (!file.exists())
				continue;
			else if (file.isDirectory())
				readFolder(file);
			else if (file.isFile())
				DirFile.files.add(file);
		}
	}

}
