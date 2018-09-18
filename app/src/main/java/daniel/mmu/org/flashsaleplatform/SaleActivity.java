package daniel.mmu.org.flashsaleplatform;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class SaleActivity extends AppCompatActivity {

    Bundle saleBundle;
    Sale sale;

    ImageView saleImage;
    TextView saleText;

    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        saleBundle = getIntent().getBundleExtra("SALE_BUNDLE");
        sale = (Sale) saleBundle.getSerializable("SALE");
        saleImage = findViewById(R.id.saleImage);
        try {
            saleImage.setImageBitmap(new HttpGetImage(SaleActivity.this).execute(sale.getSaleImage()).get());
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        saleText = findViewById(R.id.saleText);
        saleText.setText(sale.toString());
    }
}
