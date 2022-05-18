package kh.com.ipay88.sdk.demo.adapter;

/*
 * HistoryRecyclerViewAdapter
 * Demo App
 *
 * Created by kunTola on 13/2/2022.
 * Tel.017847800
 * Email.kuntola883@gmail.com
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DecimalFormat;
import java.util.List;

import kh.com.ipay88.sdk.demo.R;
import kh.com.ipay88.sdk.demo.databases.HistoryItemDto;
import kh.com.ipay88.sdk.demo.utils.StringUtils;
import kh.com.ipay88.sdk.models.IPay88PayResponse;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HistoryItemDto> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public HistoryRecyclerViewAdapter(Context context, List<HistoryItemDto> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.activity_history_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int realPosition = holder.getAdapterPosition();
        HistoryItemDto historyItemDTO = mData.get(realPosition);
        ViewHolder viewHolder = (ViewHolder) holder;
        int id = historyItemDTO.Id;
        String refNo = historyItemDTO.RefNo;
        long dateMilliseconds = historyItemDTO.CreatedDateMilliseconds;
        String json = historyItemDTO.Json;

        String fullDate = StringUtils.GetDate(dateMilliseconds, "dd/MM/yyyy hh:mm a");

        String amountStr = df.format(historyItemDTO.Amount);
        amountStr = StringUtils.GetAmountFormat(amountStr);
        amountStr += historyItemDTO.Currency.equals("USD") ? " $" : " ៛";

        String sumAmountStr = df.format(historyItemDTO.AmountSharedRef);
        sumAmountStr = StringUtils.GetAmountFormat(sumAmountStr);
        sumAmountStr = " -> " + sumAmountStr + (historyItemDTO.Currency.equals("USD") ? " $" : " ៛");

        viewHolder.textViewRefNo.setText("RefNo: " + refNo);
        viewHolder.textViewDate.setText(fullDate);
        viewHolder.textViewAmount.setText(amountStr);
        viewHolder.imageViewStatusItem.setImageResource(R.drawable.ic_exclaimation);
        viewHolder.textViewAmountUpdated.setText(sumAmountStr);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            IPay88PayResponse data = objectMapper.readValue(json, IPay88PayResponse.class);

            if (data != null) {
                boolean status = data.getStatus() == 1;
                sumAmountStr = status ? df.format(historyItemDTO.AmountSharedRef + data.getAmount()) : df.format(historyItemDTO.AmountSharedRef);
                sumAmountStr = StringUtils.GetAmountFormat(sumAmountStr);
                sumAmountStr = " -> " + sumAmountStr + (data.getCurrency().equals("USD") ? " $" : " ៛");

                viewHolder.textViewTransId.setText("TransId: " + data.getTransID());
                viewHolder.imageViewStatusItem.setImageResource(status ? R.drawable.ic_success : R.drawable.ic_failed);
                viewHolder.textViewAmountUpdated.setText(sumAmountStr);
            }

        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // MARK: - Prevent Recycler view shuffled its order after scrolled (we have to override these two methods getItemId, getItemViewType)
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewRefNo, textViewDate, textViewTransId, textViewAmount, textViewAmountUpdated;
        ImageView imageViewStatusItem;

        ViewHolder(View itemView) {
            super(itemView);
            textViewRefNo = itemView.findViewById(R.id.textViewRefNo);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTransId = itemView.findViewById(R.id.textViewTransId);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewAmountUpdated = itemView.findViewById(R.id.textViewAmountUpdated);
            imageViewStatusItem = itemView.findViewById(R.id.imageViewStatusItem);
            resetView();

            itemView.setOnClickListener(this);
        }

        private void resetView() {
            textViewRefNo.setText("Empty");
            textViewDate.setText(null);
            textViewTransId.setText(null);
            textViewAmount.setText(null);
            textViewAmountUpdated.setText(null);
            imageViewStatusItem.setImageResource(R.drawable.ic_exclaimation);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public class ViewHolderEmpty extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolderEmpty(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public HistoryItemDto getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}