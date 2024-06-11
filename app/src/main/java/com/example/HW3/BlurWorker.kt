package com.example.HW3
import android.content.Context
import android.net.Uri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.BlurTransformation


class BlurWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val imageUriString = inputData.getString("imageUri")
        val imageUri = Uri.parse(imageUriString)

        // 模糊图像
        val blurredBitmap = blurImage(imageUri)

        // 执行后续操作，例如保存模糊图像或发送通知

        return Result.success()
    }

    private fun blurImage(uri: Uri) {
        try {
            Glide.with(applicationContext)
                .asBitmap()
                .load(uri)
                .transform(BlurTransformation(25, 3))
                .submit()
                .get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}