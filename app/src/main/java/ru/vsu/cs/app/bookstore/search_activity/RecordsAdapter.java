package ru.vsu.cs.app.bookstore.search_activity;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.List;

import ru.vsu.cs.app.bookstore.R;

/**
 * Created by V on 25.11.2014.
 */
public class RecordsAdapter extends ArrayAdapter<BookObject> {

    private AQuery aq;

    public RecordsAdapter(Context context, int resource, List<BookObject> objects) {
        super(context, resource, objects);
        aq = new AQuery(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GetInfoFromActivity infoFromActivity;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list, null);
            infoFromActivity = new GetInfoFromActivity(convertView);
            convertView.setTag(infoFromActivity);
        } else {
            infoFromActivity = (GetInfoFromActivity) convertView.getTag();
        }


        infoFromActivity.populate(getItem(position)); //TODO картинку
        return convertView;
    }

    private class GetInfoFromActivity{

        //todo как загрузить картинку и где именно
        private TextView name;
        private TextView author;
        private ImageView cover;

        private GetInfoFromActivity(View view) {
            this.name = (TextView) view.findViewById(R.id.text_book_name);
            this.author = (TextView) view.findViewById(R.id.text_book_author);
            this.cover = (ImageView) view.findViewById(R.id.image_book_cover);
        }

        public void populate(BookObject book) {
            this.name.setText(book.getTitle());
            this.author.setText(book.getAuthors());
            aq.id(cover).image(book.getSmallCover().toString());
            //this.cover = cover;
        }
    }
}
