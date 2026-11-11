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

data class VpnProduct(
    val id: Long,
    val name: String,
    val price: String,
    val desc: String
)

val DarkNavy = Color(0xFF070B19)
val NeonBlue = Color(0xFF00E5FF)
val NeonGreen = Color(0xFF00E676)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val products = listOf(
            VpnProduct(1, "پاسارگاد نامحدود", "۵٬۴۹۹٬۰۰۰ تومان", "سرویس VIP سرعت بالا، تانل اختصاصی بدون قطعی مناسب همه اپراتورها"),
            VpnProduct(2, "ربکا نامحدود", "۵٬۹۹۹٬۰۰۰ تومان", "آی‌پی ثابت فوق پایدار مخصوص وب‌گردی و ترید با تضمین امنیت")
        )

        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkNavy
                ) {
                    var selectedTab by remember { mutableStateOf(0) }

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
                                                        onClick = { /* سفارش */ },
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
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VerifiedUser,
                                        contentDescription = null,
                                        tint = NeonBlue,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "کارت ویزیت دیجیتال RZVPN",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "کد معرف و نمایندگی: RZ-8890",
                                        color = NeonGreen,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "پورسانت آنی ۱۵٪ برای هر معرفی فعال",
                                        color = Color.LightGray,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
