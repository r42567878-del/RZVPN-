package com.rzvpn.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// --- ۱. ساختار پایگاه داده محلی (Room) ---
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val price: String,
    val desc: String,
    val isAvailable: Boolean = true
)

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(p: Product)
}

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun dao(): ProductDao
}

// --- ۲. رنگ‌های نئونی اختصاصی RZVPN ---
val DarkNavy = Color(0xFF070B19)
val NeonBlue = Color(0xFF00E5FF)
val NeonGreen = Color(0xFF00E676)

// --- ۳. اکتیویتی اصلی برنامه ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // راه‌اندازی دیتابیس
        val db = Room.databaseBuilder(
            applicationContext,
            AppDb::class.java,
            "rzvpn.db"
        ).fallbackToDestructiveMigration().build()
        
        val dao = db.dao()

        // اضافه کردن خودکار بسته‌های پاسارگاد و ربکا
        lifecycleScope.launch(Dispatchers.IO) {
            dao.insert(
                Product(
                    id = 1,
                    name = "پاسارگاد نامحدود",
                    price = "۵٬۴۹۹٬۰۰۰ تومان",
                    desc = "سرویس VIP سرعت بالا، تانل اختصاصی بدون قطعی مناسب همه اپراتورها"
                )
            )
            dao.insert(
                Product(
                    id = 2,
                    name = "ربکا نامحدود",
                    price = "۵٬۹۹۹٬۰۰۰ تومان",
                    desc = "آی‌پی ثابت فوق پایدار مخصوص وب‌گردی و ترید با تضمین امنیت"
                )
            )
        }

        setContent {
            // فعال‌سازی حالت راست‌چین (فارسی)
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkNavy
                ) {
                    var selectedTab by remember { mutableStateOf(0) }
                    val products by dao.getAll().collectAsState(initial = emptyList())

                    Scaffold(
                        bottomBar = {
                            NavigationBar(containerColor = Color(0xFF0F172A)) {
                                NavigationBarItem(
                                    selected = selectedTab == 0,
                                    onClick = { selectedTab = 0 },
                                    icon = { Icon(Icons.Default.Storefront, contentDescription = null) },
                                    label = { Text("فروشگاه") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = NeonBlue,
                                        selectedTextColor = NeonBlue
                                    )
                                )
                                NavigationBarItem(
                                    selected = selectedTab == 1,
                                    onClick = { selectedTab = 1 },
                                    icon = { Icon(Icons.Default.Badge, contentDescription = null) },
                                    label = { Text("کارت ویزیت") },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = NeonBlue,
                                        selectedTextColor = NeonBlue
                                    )
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(16.dp)
                        ) {
                            if (selectedTab == 0) {
                                // صفحه لیست محصولات فروشگاه
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                    item {
                                        Text(
                                            text = "شبکه اختصاصی RZVPN",
                                            color = Color.White,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "انتخاب و فعال‌سازی آنی سرویس‌های اختصاصی",
                                            color = Color.Gray,
                                            fontSize = 13.sp
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }

                                    items(products) { item ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(16.dp))
                                                .border(
                                                    1.dp,
                                                    NeonBlue.copy(alpha = 0.4f),
                                                    RoundedCornerShape(16.dp)
                                                ),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0x1AFFFFFF)
                                            )
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Text(
                                                    text = item.name,
                                                    color = NeonBlue,
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                    text = item.desc,
                                                    color = Color.LightGray,
                                                    fontSize = 13.sp
                                                )
                                                Spacer(modifier = Modifier.height(14.dp))
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = item.price,
                                                        color = NeonGreen,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 15.sp
                                                    )
                                                    Button(
                                                        onClick = { /* آماده برای درگاه یا کارت‌به‌کارت */ },
                                                        colors = ButtonDefaults.buttonColors(
                                                            containerColor = NeonBlue
                                                        ),
                                                        shape = RoundedCornerShape(10.dp)
                                                    ) {
                                                        Text(
                                                            text = "سفارش",
                                                            color = Color.Black,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                // صفحه کارت ویزیت دیجیتال و نمایندگی
                                Column(
                                    modifier = Modifier.fillMaxSize(),
