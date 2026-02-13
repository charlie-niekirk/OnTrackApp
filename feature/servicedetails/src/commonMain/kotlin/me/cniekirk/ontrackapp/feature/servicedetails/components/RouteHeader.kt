package me.cniekirk.ontrackapp.feature.servicedetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ontrackapp.feature.servicedetails.generated.resources.Res

@Composable
private fun RouteHeader(
    origin: String,
    destination: String,
    trainOperatingCompany: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
//        Text(
//            text = Res.string.route_format, origin, destination),
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.Bold,
//            maxLines = 2
//        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = trainOperatingCompany,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}