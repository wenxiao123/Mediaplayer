package com.example.wenxiao.mediaplayer.lrc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.text.TextUtils;

public class DefaultLrcBuilder implements ILrcBulider{
	private static final DefaultLrcBuilder istance = new DefaultLrcBuilder();
	
	public static final DefaultLrcBuilder getIstance(){
		return istance;
	}
	private DefaultLrcBuilder() {
	}
	@Override
	public List<LrcRow> getLrcRows(String str) {
		
		if(TextUtils.isEmpty(str)){
			return null;
		}
		BufferedReader br = new BufferedReader(new StringReader(str));
		
		List<LrcRow> lrcRows = new ArrayList<LrcRow>();
		String lrcLine;
		try {
			while((lrcLine = br.readLine()) != null){
				List<LrcRow> rows = LrcRow.createRows(lrcLine);
				if(rows != null && rows.size() > 0){
					lrcRows.addAll(rows);
				}
			}
			Collections.sort(lrcRows);
			
			for (int i = 0; i < lrcRows.size()-1; i++) {
				lrcRows.get(i).setTotalTime(lrcRows.get(i+1).getTime() - lrcRows.get(i).getTime());
			}
			lrcRows.get(lrcRows.size()-1).setTotalTime(5000);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return lrcRows;
	}

}
