package com.safeboda.core.images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.gifdecoder.GifDecoder
import com.bumptech.glide.load.model.UnitModelLoader
import com.bumptech.glide.load.resource.gif.ByteBufferGifDecoder
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.load.resource.gif.GifDrawableEncoder
import com.bumptech.glide.load.resource.gif.StreamGifDecoder
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder
import com.bumptech.glide.load.resource.transcode.DrawableBytesTranscoder
import com.bumptech.glide.load.resource.transcode.GifDrawableBytesTranscoder
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream
import java.nio.ByteBuffer

/**
 * Add GIF decoding from behind Glide's GIF .asGif() bucket
 *
 * This overrides: https://github.com/bumptech/glide/blob/52726e2a0d0fbdf123c59b15a935aa2d9d6dd3c6/library/src/main/java/com/bumptech/glide/Glide.java#L477
 */
@GlideModule
class GlideModule : AppGlideModule() {
    override fun registerComponents(
        @NonNull context: Context,
        @NonNull glide: Glide,
        @NonNull registry: Registry
    ) {
        val byteBufferGifDecoder =
            ByteBufferGifDecoder(
                context,
                registry.imageHeaderParsers,
                glide.bitmapPool,
                glide.arrayPool
            )
        val bitmapBytesTranscoder = BitmapBytesTranscoder()
        val gifDrawableBytesTranscoder = GifDrawableBytesTranscoder()

        registry
            // GIFs
            .register(
                Drawable::class.java,
                ByteArray::class.java,
                DrawableBytesTranscoder(
                    glide.bitmapPool,
                    bitmapBytesTranscoder,
                    gifDrawableBytesTranscoder
                )
            )
            .register(Bitmap::class.java, ByteArray::class.java, bitmapBytesTranscoder)
            .register(GifDrawable::class.java, ByteArray::class.java, gifDrawableBytesTranscoder)

            // GIFs
            .append(ByteBuffer::class.java, GifDrawable::class.java, byteBufferGifDecoder)
            .append(
                InputStream::class.java,
                GifDrawable::class.java,
                StreamGifDecoder(registry.imageHeaderParsers, byteBufferGifDecoder, glide.arrayPool)
            )
            .append(GifDrawable::class.java, GifDrawableEncoder())

            // GIF frames
            .append(
                GifDecoder::class.java,
                GifDecoder::class.java,
                UnitModelLoader.Factory.getInstance()
            )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}
