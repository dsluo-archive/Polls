package dev.dsluo.polls.ui.home.share;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import dev.dsluo.polls.R;

public class ShareFragment extends Fragment {

    public static final String ARG_GROUP_ID = "GROUP_ID";

    private ImageView qrImage;
    private TextView qrText;
    private Button copy;

    private String groupId;

    public ShareFragment() {
    }

    public static ShareFragment newInstance(String groupId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_GROUP_ID, groupId);

        ShareFragment fragment = new ShareFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            this.groupId = getArguments().getString(ARG_GROUP_ID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.share_fragment, container, false);

        qrImage = view.findViewById(R.id.qr_code);
        qrText = view.findViewById(R.id.group_id);
        copy = view.findViewById(R.id.copy_group_id);

        copy.setOnClickListener(button -> {
            ClipboardManager clipboard = (ClipboardManager) getContext().
                    getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clip = ClipData.newPlainText("group ID", groupId);
            clipboard.setPrimaryClip(clip);

            Snackbar.make(button, "Copied group ID to clipboard.", Snackbar.LENGTH_SHORT).show();
        });


        try {
            setQrText(groupId);
        } catch (WriterException e) {
            Snackbar.make(qrImage, "Could not make QR barcode.", Snackbar.LENGTH_INDEFINITE).show();
        }
        return view;
    }

    private void setQrText(String text) throws WriterException {
        int qrSize = getContext().getResources().getDimensionPixelSize(R.dimen.qr_code_size);

        qrText.setText(text);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix qrMatrix = writer.encode(text, BarcodeFormat.QR_CODE, qrSize, qrSize);
        Bitmap qrBitmap = Bitmap.createBitmap(qrSize, qrSize, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < qrSize; x++) {
            for (int y = 0; y < qrSize; y++) {
                int color = qrMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                qrBitmap.setPixel(x, y, color);
            }
        }
        qrImage.setImageBitmap(qrBitmap);
    }

}
