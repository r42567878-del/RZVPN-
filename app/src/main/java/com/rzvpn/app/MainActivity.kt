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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// --- لایه داده ---
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

// --- رنگ‌ها و استایل نئونی ---
val DarkNavy = Color(0xFF070B19)
val NeonBlue = Color(0xFF00E5FF)
val NeonGreen = Color(0xFF00E676)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val db = Room.databaseBuilder(applicationContext, AppDb::class.java, "rzvpn.db").build()
        val dao = db.dao()

        // درج اولیه سرویس‌های پاسارگاد و ربکا
        lifecycleScope.launch {
            dao.insert(Product(1, "پاسارگاد نامحدود", "۵٬۴۹۹٬۰۰۰ تومان", "سرویس VIP سرعت بالا، تانل اختصاصی بدون قطعی"))
            dao.insert(Product(2, "ربکا نامحدود", "۵٬۹۹۹٬۰۰۰ تومان", "آی‌پی ثابت فوق پایدار مخصوص وب‌گردی و ترید امن"))
        }

        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Surface(modifier = Modifier.fillMaxSize(), color = DarkNavy) {
                    var tab by remember { mutableStateOf(0) }
                    val products by dao.getAll().collectAsState(initial = emptyList())

                    Scaffold(
                        bottomBar = {
                            NavigationBar(containerColor = Color(0xFF0F172A)) {
                                NavigationBarItem(
                                    selected = tab == 0,
                                    onClick = { tab = 0 },
                                    icon = { Icon(Icons.Default.Storefront, null) },
                                    label = { Text("فروشگاه") },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = NeonBlue, selectedTextColor = NeonBlue)
                                )
                                NavigationBarItem(
                                    selected = tab == 1,
                                    onClick = { tab = 1 },
                                    icon = { Icon(Icons.Default.Badge, null) },
                                    label = { Text("کارت ویزیت") },
                                    colors = NavigationBarItemDefaults.colors(selectedIconColor = NeonBlue, selectedTextColor = NeonBlue)
                                )
                            }
                        }
                    ) { p ->
                        Box(modifier = Modifier.padding(p).padding(16.dp)) {
                            if (tab == 0) {
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    item {
                                        Text("شبکه اختصاصی RZVPN", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                    items(products) { item ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(16.dp))
                                                .border(1.dp, NeonBlue.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                                            colors = CardDefaults.cardColors(containerColor = Color(0x1AFFFFFF))
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Text(item.name, color = NeonBlue, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(item.desc, color = Color.LightGray, fontSize = 13.sp)
                                                Spacer(modifier = Modifier.height(12.dp))
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(item.price, color = NeonGreen, fontWeight = FontWeight.Bold)
                                                    Button(
                                                        onClick = { /* باز کردن فرم سفارش */ },
                                                        colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
                                                    ) {
                                                        Text("سفارش", color = Color.Black, fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(Icons.Default.VerifiedUser, null, tint = NeonBlue, modifier = Modifier.size(60.dp))
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("کارت ویزیت دیجیتال RZVPN", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("کد نماینده: RZ-8890 (پورسانت ۱۵٪)", color = NeonGreen)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
