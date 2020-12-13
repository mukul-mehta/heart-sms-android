package xyz.heart.sms.api.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import xyz.heart.sms.api.entity.AddContactRequest;
import xyz.heart.sms.api.entity.AddConversationRequest;
import xyz.heart.sms.api.entity.ContactBody;
import xyz.heart.sms.api.entity.ConversationBody;
import xyz.heart.sms.api.entity.UpdateContactRequest;
import xyz.heart.sms.api.entity.UpdateConversationRequest;

public interface ContactService {

    @POST("contacts/add")
    Call<Void> add(@Body AddContactRequest request);

    @POST("contacts/update_device_id")
    Call<Void> update(@Query("phone_number") String phoneNumber, @Query("device_id") long id, @Query("account_id") String accountId,
                  @Body UpdateContactRequest request);

    @POST("contacts/remove_device_id")
    Call<Void> remove(@Query("phone_number") String phoneNumber, @Query("device_id") long id, @Query("account_id") String accountId);

    @POST("contacts/clear")
    Call<Void> clear(@Query("account_id") String accountId);

    @GET("contacts")
    Call<ContactBody[]> list(@Query("account_id") String accountId);

    @GET("contacts")
    Call<ContactBody[]> list(@Query("account_id") String accountId, @Query("limit") Integer limit, @Query("offset") Integer offset);
}
