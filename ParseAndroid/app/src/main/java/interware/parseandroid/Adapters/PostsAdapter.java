package interware.parseandroid.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import interware.parseandroid.R;
import interware.parseandroid.Utils.PicassoUtils;
import interware.parseandroid.ViewHolders.PostViewHolder;
import interware.parseandroid.models.Publicacion;

/**
 * Created by chelixpreciado on 7/21/16.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private ArrayList<Publicacion> posts;
    private Context context;

    public PostsAdapter(Context context) {
        this.context = context;
        posts = new ArrayList<>();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_publicacion, null);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Publicacion publicacion = posts.get(position);
        holder.getTxtPostDescription().setText(publicacion.getDescripcion());
        int imageVisible = publicacion.getImageUrl()==null?View.GONE:View.VISIBLE;
        holder.getIvPost().setVisibility(imageVisible);
        if (imageVisible==View.VISIBLE)
            PicassoUtils.loadImage(context, publicacion.getImageUrl(), holder.getIvPost());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(ArrayList<Publicacion> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void clear(){
        posts.clear();
        notifyDataSetChanged();
    }
}
