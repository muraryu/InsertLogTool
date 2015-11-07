import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * 引数のディレクトリ以下のファイル内ログ出力箇所にファイル名と行番号追加
 * 元ファイルを上書きする
 * @author Ryuji
 *
 */
public class Main {

	public static void main(String[] args) throws IOException {

		String regexFileName	= ".*\\.java";
		String regexTargetLine	= "[^#]*[,	 ;(){}]*System\\.out\\.println\\(.*\\\".*\\\".*";
		String funcName			= "System.out.println";

		if (args.length == 0) {
			System.out.println("usage:	AddNameRow [RootDirPath or FilePath]");
			System.exit(-1);
		}

		// ルートディレクトリ
		File dir = new File(args[0]);

		// ルートディレクトリ以下のファイル取得
		Iterator<File> it = DirFile.getAllFiles(dir, regexFileName).iterator();

		// ファイルループ
		File file;
		Pattern p = Pattern.compile(regexTargetLine);
		while (it.hasNext()) {
			file = it.next();
			System.out.println("**** " + file + " ****");

			boolean replaceFileFlag = false;

			// ファイル内容
			ArrayList<String> strList = new ArrayList<String>();

			// 1行ずつ読み込みループ
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str;
			int lineNum = 1;
			while ((str = br.readLine()) != null) {

				// ファイル名と行番号を挿入する行
				if(p.matcher(str).matches()) {

					// ファイル置き換えフラグ
					replaceFileFlag = true;

					// 挿入位置を探索 関数名のあと最初に出てくる"の直後 もし先に'が出てきたら挿入しない
					int strLength = str.length();
					int offset = str.indexOf('\"', str.lastIndexOf(funcName));	// ログ出力関数名の末尾より後から 頭位置＋長さ
					while(offset < strLength) {
						if(str.charAt(offset) == '"') {
							System.out.println("old ["+ file.getName() + "," + lineNum +  "]" + str);
							System.out.println("new ["+ file.getName() + "," + lineNum +  "]" + new String(new StringBuilder(str).insert(offset+1, file.getName() + "," + lineNum + ",")));
							str = new String(new StringBuilder(str).insert(offset+1, file.getName() + "," + lineNum + ","));
							replaceFileFlag = true;	// ファイル置き換えフラグ真
							break;
						}
						else if(str.charAt(offset) == '\'') {
							break;
						}
						offset++;
					}

				}

				// ファイル置き換えのためファイル内容保持
				strList.add(str);

				lineNum++;
			}
			System.out.println("");
			// 読み出しファイルクローズ
			br.close();

			// 挿入した場合はファイル置き換え
			if(replaceFileFlag) {
				file.delete();
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file.getAbsolutePath())));

				// 書き込み
				Iterator<String> itStrList = strList.iterator();
				while(itStrList.hasNext()) {
					bw.write(itStrList.next());
					bw.newLine();
				}

				// ファイルクローズ
				bw.close();

			}

/*
 *
System.out.println(file.getName() + "," + lineNum + " : " + str);
 */
		}

	}

}
