import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class DirFile {

	static private ArrayList<File> files;
 
	static public ArrayList<File> getAllFiles(File dir) {

		DirFile.files = new ArrayList<File>();
		readFolder(dir);
		return files;

	}

	/**
	 * ルートディレクトリ以下のすべてのファイルのうち正規表現にマッチするファイルを取得する
	 * @param dir
	 * @param regex
	 * @return
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
