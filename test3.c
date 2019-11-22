static int zero12v_decode_frame(AVCodecContext *avctx, void *data,
                                int *got_frame, AVPacket *avpkt)
{
    int line, ret;
    const int width = avctx->width;
    AVFrame *pic = data;
    uint16_t *y, *u, *v;
    const uint8_t *line_end, *src = avpkt->data;
    int stride = avctx->width * 8 / 3;
    if (width <= 1 || avctx->height <= 0) {
        av_log(avctx, AV_LOG_ERROR, "Dimensions %dx%d not supported.\n", width, avctx->height);
        return AVERROR_INVALIDDATA;
    }
    if (   avctx->codec_tag == MKTAG('0', '1', '2', 'v')
        && avpkt->size % avctx->height == 0
        && avpkt->size / avctx->height * 3 >= width * 8)
        stride = avpkt->size / avctx->height;
    if (avpkt->size < avctx->height * stride) {
        av_log(avctx, AV_LOG_ERROR, "Packet too small: %d instead of %d\n",
               avpkt->size, avctx->height * stride);
        return AVERROR_INVALIDDATA;
    }
    if ((ret = ff_get_buffer(avctx, pic, 0)) < 0)
        return ret;
    pic->pict_type = AV_PICTURE_TYPE_I;
    pic->key_frame = 1;
    line_end = avpkt->data + stride;
    for (line = 0; line < avctx->height; line++) {
        uint16_t y_temp[6] = {0x8000, 0x8000, 0x8000, 0x8000, 0x8000, 0x8000};
        uint16_t u_temp[3] = {0x8000, 0x8000, 0x8000};
        uint16_t v_temp[3] = {0x8000, 0x8000, 0x8000};
        int x;
        y = (uint16_t *)(pic->data[0] + line * pic->linesize[0]);
        u = (uint16_t *)(pic->data[1] + line * pic->linesize[1]);
        v = (uint16_t *)(pic->data[2] + line * pic->linesize[2]);
        for (x = 0; x < width; x += 6) {
            uint32_t t;
            if (width - x < 6 || line_end - src < 16) {
                y = y_temp;
                u = u_temp;
                v = v_temp;
            }
            if (line_end - src < 4)
                break;
            t = AV_RL32(src);
            src += 4;
            *u++ = t <<  6 & 0xFFC0;
            *y++ = t >>  4 & 0xFFC0;
            *v++ = t >> 14 & 0xFFC0;
            if (line_end - src < 4)
                break;
            t = AV_RL32(src);
            src += 4;
            *y++ = t <<  6 & 0xFFC0;
            *u++ = t >>  4 & 0xFFC0;
            *y++ = t >> 14 & 0xFFC0;
            if (line_end - src < 4)
                break;
            t = AV_RL32(src);
            src += 4;
            *v++ = t <<  6 & 0xFFC0;
            *y++ = t >>  4 & 0xFFC0;
            *u++ = t >> 14 & 0xFFC0;
            if (line_end - src < 4)
                break;
            t = AV_RL32(src);
            src += 4;
            *y++ = t <<  6 & 0xFFC0;
            *v++ = t >>  4 & 0xFFC0;
            *y++ = t >> 14 & 0xFFC0;
            if (width - x < 6)
                break;
        }
        if (x < width) {
            y = x   + (uint16_t *)(pic->data[0] + line * pic->linesize[0]);
            u = x/2 + (uint16_t *)(pic->data[1] + line * pic->linesize[1]);
            v = x/2 + (uint16_t *)(pic->data[2] + line * pic->linesize[2]);
            memcpy(y, y_temp, sizeof(*y) * (width - x));
            memcpy(u, u_temp, sizeof(*u) * (width - x + 1) / 2);
            memcpy(v, v_temp, sizeof(*v) * (width - x + 1) / 2);
        }
        line_end += stride;
        src = line_end - stride;
    }
    *got_frame = 1;
    return avpkt->size;
}
