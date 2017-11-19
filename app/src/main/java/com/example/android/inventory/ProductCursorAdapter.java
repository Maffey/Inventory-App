package com.example.android.inventory;

/**
 * Created by Maffey on 2017-07-19.
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory.data.ProductContract.ProductEntry;

import static com.example.android.inventory.R.id.quantity;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView nameView = (TextView) view.findViewById(R.id.name);
        TextView priceView = (TextView) view.findViewById(R.id.price);
        TextView quantityView = (TextView) view.findViewById(quantity);
        Button button = (Button) view.findViewById(R.id.sell_button);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_catalog);

        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_NAME));
        String price = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_PRICE));
        final int quantity = cursor.getInt(cursor.getColumnIndex(ProductEntry.COLUMN_QUANTITY));
        final Uri uri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI,
                cursor.getInt(cursor.getColumnIndex(ProductEntry._ID)));
        String imageString = cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_IMAGE));
        // Make sure the string is converted into uri data type.
        Uri imageUri = Uri.parse(imageString);

        // Populate fields with extracted properties
        nameView.setText(name);
        priceView.setText(price);
        quantityView.setText(String.valueOf(quantity));
        imageView.setImageURI(imageUri);
        imageView.invalidate();


        // Set OnClickListener to change quantity in real-time, whenever user presses "SELL" button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0) {
                    int quantityReduced = quantity - 1;
                    ContentValues value = new ContentValues();
                    value.put(ProductEntry.COLUMN_QUANTITY, quantityReduced);
                    context.getContentResolver().update(uri, value, null, null);
                } else {
                    Toast.makeText(context, context.getString(R.string.quantity_limit), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}