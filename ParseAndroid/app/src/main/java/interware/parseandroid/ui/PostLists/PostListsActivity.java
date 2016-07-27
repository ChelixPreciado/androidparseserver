package interware.parseandroid.ui.PostLists;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import interware.parseandroid.Adapters.PostsAdapter;
import interware.parseandroid.R;
import interware.parseandroid.models.Publicacion;
import interware.parseandroid.parseserver.PostsHandler;
import interware.parseandroid.persistencia.UserSession;
import interware.parseandroid.ui.AddPost.AddPostFragment;
import interware.parseandroid.ui.AddPost.AddPostFragmentListener;
import interware.parseandroid.ui.Login.LoginActivity;
import interware.parseandroid.ui.ParseappActivity;

public class PostListsActivity extends ParseappActivity implements View.OnClickListener, AddPostFragmentListener{

    public RecyclerView rvPosts;
    public PostsAdapter postsAdapter;
    private EditText edWritePost;
    private ImageButton btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_lists);

        rvPosts = (RecyclerView)findViewById(R.id.rv_posts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvPosts.setLayoutManager(mLayoutManager);
        rvPosts.setItemAnimator(new DefaultItemAnimator());
        postsAdapter = new PostsAdapter(getApplicationContext());
        fillAdapter();

        edWritePost = (EditText)findViewById(R.id.ed_write_post);
        edWritePost.setOnClickListener(this);

        btnLogout = (ImageButton)findViewById(R.id.btn_log_out);
        btnLogout.setOnClickListener(this);
    }

    private void fillAdapter(){
        showLoader(true);
        if (postsAdapter!=null)
            postsAdapter.clear();
        PostsHandler.getPosts(new PostsHandler.GetPostCallback() {
            @Override
            public void onPostsObtained(ArrayList<Publicacion> posts) {
                postsAdapter.setPosts(posts);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ed_write_post:
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                showPostDialog();
                break;
            case R.id.btn_log_out:
                logOut();
                break;
        }
    }

    private void logOut(){
        UserSession userSession = new UserSession(getApplicationContext());
        userSession.logOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showPostDialog(){
        AddPostFragment postDialog = AddPostFragment.newInstance();
        postDialog.show(getFragmentManager(), "AddpostDialog");
    }

    @Override
    public void onPostWrited(String postMsg, String postImageUrl) {
        showLoader(true);
        PostsHandler.doPost(postMsg, postImageUrl, new PostsHandler.postedPost() {
            @Override
            public void posted(boolean posted) {
                showLoader(false);
                fillAdapter();
            }
        });
    }
}
