package daniel.mmu.org.flashsaleplatform;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SalesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate this fragment's layout.
        return inflater.inflate(R.layout.sales_main, container, false);
    }
}
