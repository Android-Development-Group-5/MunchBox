import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.group5.munchbox.Comment
import com.group5.munchbox.R
class CommentsAdapter(private val context: Context, private val CommentsItems: MutableList<Comment>): RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val commentTextView: TextView
        val usernameTextView: TextView

        init {
            commentTextView = itemView.findViewById(R.id.comment_message)
            usernameTextView = itemView.findViewById(R.id.comment_username)
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
    }

    override fun getItemCount(): Int {
        return CommentsItems.size
    }
}