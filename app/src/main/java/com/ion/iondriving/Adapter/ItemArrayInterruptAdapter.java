package com.ion.iondriving.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.ion.iondriving.R;

import java.util.ArrayList;
import java.util.List;

import static com.ion.iondriving.CsvDisplayController.speeed;
import static com.ion.iondriving.macro.MacroConstant.acceleration_value;
import static com.ion.iondriving.macro.MacroConstant.breaking_value;

public class ItemArrayInterruptAdapter extends  ArrayAdapter<String[]>{
	private  Context mContext;

	public static List<LatLng> 	latLngs = new ArrayList<>();
	public static List<LatLng> allLatLngs = new ArrayList<>();
	public static List<LatLng> breakingLat=new ArrayList<>();
	public  static  int number=0;
	private List<String[]> scoreList = new ArrayList<String[]>();

	static class ItemViewHolder {
		TextView dateOfInterrupt;
		TextView StartTime;
		TextView EndTime;
		TextView Duration;
		TextView InterruptType;
		TextView Interruptstatus;
		TextView Crng;
		TextView Date;
		TextView Time;
		TextView Acc_X;
		TextView Acc_Y;
		TextView Acc_Z;
		TextView Singal_Strength;
		TextView Gyro_X;
		TextView breakingValue;
		TableRow tableHeader;


	}

	public ItemArrayInterruptAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		mContext=context;

        latLngs.clear();
		speeed.clear();
		number=0;
		Toast.makeText(context,""+latLngs.size(),Toast.LENGTH_SHORT).show();
        allLatLngs.clear();
		breakingLat.clear();


	}

	@Override
	public void add(String[] object) {
		scoreList.add(object);
		number=object.length;
		super.add(object);
	}

	@Override
	public int getCount() {
		return this.scoreList.size();
	}

	@Override
	public String[] getItem(int index) {
		return this.scoreList.get(index);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ItemViewHolder viewHolder;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.csv_view_interupt, parent, false);

			viewHolder = new ItemViewHolder();
			viewHolder.tableHeader=(TableRow)row.findViewById(R.id.tableRow1);
			viewHolder.dateOfInterrupt = (TextView) row.findViewById(R.id.name_Date);
			viewHolder.StartTime = (TextView) row.findViewById(R.id.Start_Time);
			viewHolder.EndTime = (TextView) row.findViewById(R.id.End_Time);
			viewHolder.Duration = (TextView) row.findViewById(R.id.name_Duration);
			viewHolder.InterruptType = (TextView) row.findViewById(R.id.Interrupt_Type);
			viewHolder.Interruptstatus = (TextView) row.findViewById(R.id.Interrupt_Status);

			viewHolder.Crng = (TextView) row.findViewById(R.id.Crng);
			viewHolder.Date = (TextView) row.findViewById(R.id.Date);
			viewHolder.Time = (TextView) row.findViewById(R.id.Time);
//			viewHolder.Acc_X = (TextView) row.findViewById(R.id.Acc_X);
//			viewHolder.Acc_Y = (TextView) row.findViewById(R.id.Acc_Y);
//			viewHolder.Acc_Z = (TextView) row.findViewById(R.id.Acc_Z);
			viewHolder.Singal_Strength = (TextView) row.findViewById(R.id.Signal_Strength);

			viewHolder.Gyro_X = (TextView) row.findViewById(R.id.Gyro_X);
			viewHolder.breakingValue=(TextView) row.findViewById(R.id.Breakings);

			row.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder)row.getTag();
		}

			  String[] stat = getItem(position);
    /*if(position == 0){
		viewHolder.tableHeader.setBackgroundColor(mContext.getResources().getColor(R.color.headerColor));
	}*/


		viewHolder.dateOfInterrupt.setText(stat[1]);
		viewHolder.StartTime.setText(stat[2]);
		viewHolder.EndTime.setText(stat[3]);
		viewHolder.Duration.setText(stat[7]);
		viewHolder.InterruptType.setText(stat[8]);
		viewHolder.Interruptstatus.setText(stat[9]);
		viewHolder.Crng.setText(stat[10]);
		viewHolder.Date.setText(stat[11]);
		viewHolder.Time.setText(stat[12]);
		/*viewHolder.Acc_X.setText(stat[13]);
		viewHolder.Acc_Y.setText(stat[14]);
		viewHolder.Acc_Z.setText(stat[15]);*/
		viewHolder.Singal_Strength.setText(stat[16]);
		viewHolder.Gyro_X.setText(stat[13]);
		viewHolder.breakingValue.setText(stat[17]);

		/*int values= Integer.parseInt(stat[17]);

		double lat= Double.parseDouble(stat[1]);
		double lon= Double.parseDouble(stat[2]);
		allLatLngs.add(new LatLng(lat,lon));

		if(acceleration_value == values) {
			latLngs.add(new LatLng(lat,lon));
	//		number=position;
		}else if(breaking_value == values)  {
			breakingLat.add(new LatLng(lat,lon));
	//		number=position;
		}*/

		return row;

	}


}
