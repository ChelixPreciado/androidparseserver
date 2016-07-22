package interware.parseandroid.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import interware.parseandroid.R;

/**
 * Created by chelixpreciado on 7/21/16.
 */
public class PostViewHolder extends RecyclerView.ViewHolder {

    private TextView txtPostDescription;
    private ImageView ivPost;

    public PostViewHolder(View itemView) {
        super(itemView);
    }

    public TextView getTxtPostDescription() {
        if (txtPostDescription==null)
            txtPostDescription = (TextView)itemView.findViewById(R.id.txt_description);
        return txtPostDescription;
    }

    public ImageView getIvPost() {
        if (ivPost==null)
            ivPost = (ImageView)itemView.findViewById(R.id.iv_post);
        return ivPost;
    }
}
