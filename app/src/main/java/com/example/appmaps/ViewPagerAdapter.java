package com.example.appmaps;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.SlideViewHolder> {

    private List<Slide> slides;
    private Context context;

    public ViewPagerAdapter(Context context, List<Slide> slides) {
        this.context = context;
        this.slides = slides;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slide, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        Slide slide = slides.get(position);
        holder.image.setImageResource(slide.getImage());
        holder.title.setText(slide.getTitle());
        holder.description.setText(slide.getDescription());
    }

    @Override
    public int getItemCount() {
        return slides.size();
    }

    static class SlideViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.slideImage);
            title = itemView.findViewById(R.id.slideTitle);
            description = itemView.findViewById(R.id.slideDescription);
        }
    }
}