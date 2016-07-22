package interware.parseandroid.ui.PostLists;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import interware.parseandroid.Adapters.PostsAdapter;
import interware.parseandroid.R;
import interware.parseandroid.models.Publicacion;
import interware.parseandroid.parseserver.PostsHandler;
import interware.parseandroid.ui.ParseappActivity;

public class PostListsActivity extends ParseappActivity {

    public RecyclerView rvPosts;
    public PostsAdapter postsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_lists);

        rvPosts = (RecyclerView)findViewById(R.id.rv_posts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvPosts.setLayoutManager(mLayoutManager);
        rvPosts.setItemAnimator(new DefaultItemAnimator());
        fillAdapter();
    }

    private void fillAdapter(){
        showLoader(true);
        PostsHandler.getPosts(new PostsHandler.GetPostCallback() {
            @Override
            public void onPostsObtained(ArrayList<Publicacion> posts) {
                postsAdapter = new PostsAdapter(getApplicationContext(), posts);
                rvPosts.setAdapter(postsAdapter);
                showLoader(false);
            }

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(PostListsActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                showLoader(false);
            }
        });
    }
}
