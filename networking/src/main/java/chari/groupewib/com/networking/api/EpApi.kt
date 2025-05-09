package ghoudan.anfaSolution.com.networking.api

import chari.groupewib.com.app_models.SingleItemResponse
import chari.groupewib.com.networking.entity.ItemEntity
import chari.groupewib.com.networking.entity.ItemStockEntity
import chari.groupewib.com.networking.entity.PackingListEntity
import chari.groupewib.com.networking.entity.StockSaisieEntity
import chari.groupewib.com.networking.request.CreateClientRequest
import chari.groupewib.com.networking.request.CreateItemRequest
import chari.groupewib.com.networking.request.CreateSupplierRequest
import chari.groupewib.com.networking.request.ItemEntityResponse
import chari.groupewib.com.networking.request.PackingListUpdate
import chari.groupewib.com.networking.request.PurchaseItemEntityResponse
import chari.groupewib.com.networking.request.PurchaseOrderHeaderRequest
import chari.groupewib.com.networking.request.PurchaseOrderHeaderResult
import chari.groupewib.com.networking.request.PurchaseOrderLinesRequest
import chari.groupewib.com.networking.request.SalesOrderHeaderRequest
import chari.groupewib.com.networking.request.SalesOrderHeaderResult
import chari.groupewib.com.networking.request.SalesOrderLinesRequest
import chari.groupewib.com.networking.request.UpdatePurchaseHeaderRequest
import chari.groupewib.com.networking.request.UpdateSalesHeaderRequest
import chari.groupewib.com.networking.response.SalesOrderLinesResponse
import ghoudan.anfaSolution.com.app_models.CommandAchat
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.app_models.PurchaseOrder
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.networking.entity.*
import ghoudan.anfaSolution.com.networking.request.LoginRequest
import ghoudan.anfaSolution.com.networking.response.EPApiCollectionResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface EpApi {

    //**** Purchase
    //returns 204 as valid request
    @DELETE("Company('ABFISH-140')/Commande_achat(Document_Type='{type}',No='{num}')")
    suspend fun deletePurchaseCommandsHeader(
//        @Header("if-Match") Etag: String,
        @Path("type") type: String,
        @Path("num") document8no: String
    ): Response<Unit>


    @GET("Company('ABFISH-140')/Commande_achat(Document_Type='{type}',No='{num}')")
    suspend fun getPurchaseCommandsHeader(
        @Path("type") type: String,
        @Path("num") document8no: String): Response<PurchaseOrderHeaderResult>

    //header on request
    @PATCH("Company('ABFISH-140')/Commande_achat(Document_Type='{type}',No='{num}')")
    suspend fun updatePurchaseCommandsHeader(
        @Header("if-Match") Etag: String,
        @Path("type") type: String,
        @Path("num") document8no: String,
        @Body order: UpdatePurchaseHeaderRequest
    ): Response<PurchaseOrderHeaderResult>

    @POST("Company('ABFISH-140')/Commande_achat")
    suspend fun addPurchaseCommandsHeader(@Body order: PurchaseOrderHeaderRequest): Response<PurchaseOrderHeaderResult>


    @GET("Company('ABFISH-140')/Achat_Lignes(Document_Type='{type}',Document_No='{num}',Line_No={numLine})")
    suspend fun getPurchaseCommandsLine(
        @Path("type") type: String,
        @Path("num") document8no: String,
        @Path("numLine") numLine: Int
    ): Response<SalesOrderLinesResponse>
    //********** purchase lines
    //returns 204 as valid request

    @DELETE("Company('ABFISH-140')/Achat_Lignes(Document_Type='{type}',Document_No='{num}',Line_No={numLine})")
    suspend fun deletePurchaseCommandsLine(
//        @Header("if-Match") Etag: String,
        @Path("type") type: String,
        @Path("num") document8no: String,
        @Path("numLine") numLine: Int
    ): Response<Unit>

    //header on request
    @PATCH("Company('ABFISH-140')/Achat_Lignes(Document_Type='{type}',Document_No='{num}',Line_No={numLine})")
    suspend fun updatePurchaseCommandsLine(
        @Header("if-Match") Etag: String,
        @Path("type") type: String,
        @Path("num") document8no: String,
        @Path("numLine") numLine: Int,
        @Body order: PurchaseOrderLinesRequest
    ): Response<SalesOrderLinesResponse>

    @POST("Company('ABFISH-140')/Achat_Lignes")
    suspend fun addPurchaseCommandsLine(@Body order: PurchaseOrderLinesRequest): Response<SalesOrderLinesResponse>

    //get purchase lines by document type and document no
    @GET("Company('ABFISH-140')/Achat_Lignes(Document_Type='{type}',Document_No='{num}')")
    suspend fun getPurchaseCommandsLines(
        @Query("type") type: String,
        @Query("num") document8no: String
    ): Response<List<ItemEntity>>

    //************ Sales
    //returns 204 as valid request

    @DELETE("Company('ABFISH-140')/Sales_Order_Header(Document_Type='{type}',No='{num}')")
    suspend fun deleteSalesCommandsHeader(
//        @Header("if-Match") Etag: String,
        @Path("type") type: String,
        @Path("num") document8no: String
    ): Response<Unit>


    //header on request
    @PATCH("Company('ABFISH-140')/Sales_Order_Header(Document_Type='{type}',No='{num}')")
    suspend fun updateSalesCommandsHeader(
        @Header("if-Match") Etag: String,
        @Path("type") type: String,
        @Path("num") document8no: String,
        @Body order: UpdateSalesHeaderRequest
    ): Response<SalesOrderHeaderResult>

    @GET("Company('ABFISH-140')/Sales_Order_Header(Document_Type='{type}',No='{num}')")
    suspend fun getSalesCommandsHeader(
        @Path("type") type: String,
        @Path("num") document8no: String): Response<SalesOrderHeaderResult>

    @POST("Company('ABFISH-140')/Sales_Order_Header")
    suspend fun addSalesCommandsHeader(@Body order: SalesOrderHeaderRequest): Response<SalesOrderHeaderResult>


    //returns 204 as valid request
    @DELETE("Company('ABFISH-140')/Sales_Order_Lines(Document_Type='{type}',Document_No='{num}',Line_No={numLine})")
    suspend fun deleteSalesCommandsLine(
        @Header("if-Match") Etag: String,
        @Path("type") type: String,
        @Path("num") document8no: String,
        @Path("numLine") numLine: Int
    ): Response<ResponseBody>


    //header on request
    @PATCH("Company('ABFISH-140')/Sales_Order_Lines(Document_Type='{type}',Document_No='{num}',Line_No={numLine})")
    suspend fun updateSalesCommandsLine(
        @Header("if-Match") Etag: String,
        @Path("type") type: String,
        @Path("num") document8no: String,
        @Path("numLine") numLine: Int,
        @Body order: SalesOrderLinesRequest
    ): Response<SalesOrderLinesResponse>

    @GET("Company('ABFISH-140')/Sales_Order_Lines(Document_Type='{type}',Document_No='{num}',Line_No={numLine})")
    suspend fun getSalesCommandsLine(
        @Path("type") type: String,
        @Path("num") document8no: String,
        @Path("numLine") numLine: Int
    ): Response<SalesOrderLinesResponse>

    @POST("Company('ABFISH-140')/Sales_Order_Lines")
    suspend fun addSalesCommandsLine(@Body order: SalesOrderLinesRequest): Response<SalesOrderLinesResponse>

    //get sales lines by document type and document no
    @GET("Company('ABFISH-140')/Sales_Order_Lines")
    suspend fun getSalesCommandsLines(
        @Query("\$filter") document8no: String
    ): EPApiCollectionResponse<List<ItemEntityResponse>>

    @GET("Company('ABFISH-140')/Sales_Header")
    suspend fun getSelesCommands(): EPApiCollectionResponse<List<CommandAchat>>


    @GET("Company('ABFISH-140')/Achat_Lignes")
    suspend fun getPurchaseCommandsLines(
        @Query("\$filter") document8no: String
    ): EPApiCollectionResponse<List<PurchaseItemEntityResponse>>

    @GET("Company('ABFISH-140')/Commande_achat_List")
    suspend fun getPorchesCommands(): EPApiCollectionResponse<List<PurchaseOrder>>

    //*******clients
    @GET("Company('ABFISH-140')/List_Clients")
    suspend fun getClients(): EPApiCollectionResponse<List<CustomerAnfaEntity>>

    @GET("Company('ABFISH-140')/Fiche_client(No='{num}')")
    suspend fun getSingleClients(
        @Path("num") clientId: String
    ): Response<CustomerAnfa>
    @GET("Company('ABFISH-140')/Fiche_Article(No='{num}')")
    suspend fun getSingleItem(
        @Path("num") clientId: String
    ): Response<SingleItemResponse>

    @POST("Company('ABFISH-140')/Fiche_client")
    suspend fun addClient(@Body order: CreateClientRequest): Response<CustomerAnfa>

    @PATCH("Company('ABFISH-140')/Fiche_client(No='{No}')")
    suspend fun updateClient(
        @Header("if-Match") Etag: String,
        @Path("No") type: String,
        @Body order: CreateClientRequest
    ): Response<CustomerAnfa>
    @POST("Company('ABFISH-140')/Fiche_Article")
    suspend fun addItem(@Body order: CreateItemRequest): Response<SingleItemResponse>

    @PATCH("Company('ABFISH-140')/Fiche_Article(No='{No}')")
    suspend fun updateItem(
        @Header("if-Match") Etag: String,
        @Path("No") type: String,
        @Body order: CreateItemRequest
    ): Response<SingleItemResponse>


    @DELETE("Company('ABFISH-140')/Fiche_client(No='{No}')")
    suspend fun deleteClient(
        @Header("if-Match") Etag: String,
        @Path("No") type: String
    ): Response<Unit>


    //***********Suppliers
    @POST("Company('ABFISH-140')/Fiche_Fournisseur")
    suspend fun addSupplier(@Body order: CreateSupplierRequest): Response<SupplierAnfa>

    @GET("Company('ABFISH-140')/List_Fournisseurs")
    suspend fun getSuppliers(): EPApiCollectionResponse<List<SupplierAnfaEntity>>

    @GET("Company('ABFISH-140')/Fiche_Fournisseur(No='{num}')")
    suspend fun getSingleSuppliers(
        @Path("num") clientId: String
    ): Response<SupplierAnfa>

    @PATCH("Company('ABFISH-140')/Fiche_Fournisseur(No='{No}')")
    suspend fun updateSupplier(
        @Header("if-Match") Etag: String,
        @Path("No") type: String,
        @Body order: CreateSupplierRequest
    ): Response<SupplierAnfa>

    @DELETE("Company('ABFISH-140')/Fiche_Fournisseur(No='{No}')")
    suspend fun deleteSupplier(
        @Header("if-Match") Etag: String,
        @Path("No") type: String
    ): Response<Unit>

    @GET("Company('ABFISH-140')/Fiche_Article")
    suspend fun getArticles(): EPApiCollectionResponse<List<ItemEntity>>


    @GET("Company('ABFISH-140')/Lots_Articles_Disponibles")
    suspend fun getStock(): EPApiCollectionResponse<List<ItemStockEntity>>

    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequest)
            : Response<Unit>

    @POST("users/login")
    suspend fun sendSmsCode(@Body loginRequest: LoginRequest)
            : Response<Unit>


    @GET("Company('ABFISH-140')/Users")
    suspend fun getUserInfo(): EPApiCollectionResponse<List<UserEntity>>


    @GET("Company('ABFISH-140')/StockSaisieList")
    suspend fun getStockSaisieList(): EPApiCollectionResponse<List<StockSaisieEntity>>

    @GET("Company('ABFISH-140')/PackingList")
    suspend fun getPackingList(
        @Query("\$filter") filter :String
    ): EPApiCollectionResponse<List<PackingListEntity>>

    @GET("Company('ABFISH-140')/PackingList")
    suspend fun getPackingListByColisNumber(
        @Query("\$filter") filter :String
    ): EPApiCollectionResponse<List<PackingListEntity>>

    @GET("Company('ABFISH-140')/PackingList")
    suspend fun getPackingListByLigneAch(
        @Query("\$filter") filter :String
    ): EPApiCollectionResponse<List<PackingListEntity>>

    @PATCH
    suspend fun updatePackingList(
        @Url url: String,
        @Header("if-Match") Etag: String,
        @Body update: PackingListUpdate
    ): Response<PackingListEntity>

}
