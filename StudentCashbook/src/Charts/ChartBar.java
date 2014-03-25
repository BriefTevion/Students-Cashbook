package Charts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studentcashbook.R;

public class ChartBar extends Fragment{

	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
      View windows = inflater.inflate(R.layout.chartbar_frag, container, false);
        ((TextView)windows.findViewById(R.id.textView)).setText("Bar");
        return windows;
}
	 
}
