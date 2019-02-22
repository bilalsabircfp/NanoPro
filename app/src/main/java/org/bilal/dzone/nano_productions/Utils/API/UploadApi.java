package org.bilal.dzone.nano_productions.Utils.API;

import org.bilal.dzone.nano_productions.Utils.ResponseData;
import org.bilal.dzone.nano_productions.Utils.Url;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface UploadApi {

    @Multipart
    @POST(Url.upload_files)
    Call<ResponseData> uploadData(@PartMap Map<String, RequestBody> map,
                                  @Part List<MultipartBody.Part> fileList);
}
