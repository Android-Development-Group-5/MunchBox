import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.storage.FirebaseStorage
import com.group5.munchbox.Comment
import com.group5.munchbox.R
class CommentsAdapter(private val context: Context, private val CommentsItems: MutableList<Comment>): RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val commentTextView: TextView
        val usernameTextView: TextView
        val profilePictureView : ImageView
        init {
            commentTextView = itemView.findViewById(R.id.comment_message)
            usernameTextView = itemView.findViewById(R.id.comment_username)
            profilePictureView = itemView.findViewById(R.id.profile_picture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.comment_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commentItem = CommentsItems[position]
        holder.commentTextView.text = commentItem.message
        holder.usernameTextView.text = commentItem.userEmail
        Glide.with(context).load(FirebaseStorage.getInstance().getReference("images").child("users").child("${commentItem.userId}.png"))
            //.placeholder(R.drawable.baseline_downloading_24) //loading
            .error(R.drawable.outline_person_24) //in case of user not having an image
            .centerCrop() // scale image to fill the entire ImageView
            .transform(RoundedCorners(100)).into(holder.profilePictureView)

    }

    override fun getItemCount(): Int {
        return CommentsItems.size
    }
}