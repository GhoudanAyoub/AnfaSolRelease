package ghoudan.anfaSolution.com.networking.entity.mapper

import chari.groupewib.com.app_models.Item
import chari.groupewib.com.app_models.ItemStock
import chari.groupewib.com.networking.entity.ItemEntity
import chari.groupewib.com.networking.entity.ItemStockEntity
import chari.groupewib.com.networking.request.ItemEntityResponse
import chari.groupewib.com.networking.request.PurchaseItemEntityResponse
import ghoudan.anfaSolution.com.app_models.CustomerAnfa
import ghoudan.anfaSolution.com.app_models.SupplierAnfa
import ghoudan.anfaSolution.com.app_models.User
import ghoudan.anfaSolution.com.networking.entity.CustomerAnfaEntity
import ghoudan.anfaSolution.com.networking.entity.SupplierAnfaEntity
import ghoudan.anfaSolution.com.networking.entity.UserEntity
import java.util.UUID


internal fun ItemStockEntity.toAppModel() = ItemStock(
    code = code,
    description = description,
    unitCode = unitCode,
    type = type,
    quantity = quantity,
    etag = etag,
    Document_No = Document_No,
    Item_No = Item_No,
    family = family
)

internal fun ItemEntity.toAppModel() = Item(
    code = code ?: UUID.randomUUID().toString(),
    description = description,
    unitCode = unitCode,
    unitCost = unitCost,
    etag = etag,
    Line_No=Line_No,
    Item_Category_Code=Item_Category_Code,
    Blocked=Blocked
)

internal fun ItemEntityResponse.toAppModel() = Item(
    code = code ?: UUID.randomUUID().toString(),
    description = description,
    unitCode = unitCode,
    unitCost = unitCost,
    etag = etag,
    Line_No = Line_No,
    Item_Category_Code=Item_Category_Code,
    Type = Type,
    Family=Family,
    Blocked=Blocked,
    itemUnits = itemUnits,
    itemWeight = itemWeight,
    parcel = parcel
)
internal fun PurchaseItemEntityResponse.toAppModel() = Item(
    code = code ?: UUID.randomUUID().toString(),
    description = description,
    unitCode = unitCode,
    unitCost = unitCost,
    etag = etag,
    Line_No = Line_No,
    Item_Category_Code=Item_Category_Code,
    Type = Type,
    Family=Family,
    Blocked=Blocked,
    itemUnits = itemUnits,
    itemWeight = itemWeight,
    parcel = parcel
)

internal fun CustomerAnfaEntity.toAppModel() = CustomerAnfa(
    code = code.toString(),
    name = name,
    description = description,
    Phone_No = Phone_No,
    City = City,
    Post_Code = Post_Code,
    Address = Address,
    Sector = Sector,
    agent_code = agent_code,
    Blocked = Blocked,
    etag = etag
)

internal fun SupplierAnfaEntity.toAppModel() = SupplierAnfa(
    code = code,
    name = name,
    Phone_No = Phone_No,
    City = City,
    Post_Code = Post_Code,
    region = region,
    etag = etag,
    vpg = vpg
)

internal fun UserEntity.toAppModel() = User(
    password = password,
    userId = userId ?: UUID.randomUUID().toString(),
    username = username
)
