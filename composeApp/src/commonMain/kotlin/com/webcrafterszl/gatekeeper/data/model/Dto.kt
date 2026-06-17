package com.webcrafterszl.gatekeeper.data.model

import kotlinx.serialization.Serializable

// =================================================================================
// 1. AUTH DOMAIN
// =================================================================================

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val message: String,
    val token: String
)

@Serializable
data class ForgotPasswordRequest(
    val email: String
)

@Serializable
data class ResetPasswordRequest(
    val token: String,
    val newPassword: String
)

@Serializable
data class SetupPasswordRequest(
    val email: String,
    val code: String,
    val password: String
)

@Serializable
data class ValidateOtpRequest(
    val code: String
)

// =================================================================================
// 2. ADMIN DOMAIN
// =================================================================================

@Serializable
data class CreateManagerRequest(
    val fullName: String,
    val email: String
)

@Serializable
data class AppUserResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val role: Role,
    val isActive: Boolean
)

// =================================================================================
// 3. MANAGER DOMAIN
// =================================================================================

@Serializable
data class AccessPointResponse(
    val id: Long,
    val locationDescription: String,
    val mqttIdentifier: String,
    val isUnderMaintenance: Boolean
)

@Serializable
data class CreateAccessPointRequest(
    val locationDescription: String
)

@Serializable
data class CardholderResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val isActive: Boolean,
    val credentials: List<RfidCredentialResponse> = emptyList()
)

@Serializable
data class CreateCardholderRequest(
    val fullName: String,
    val email: String
)

@Serializable
data class UpdateCardholderRequest(
    val fullName: String?,
    val email: String?,
    val isActive: Boolean?
)

@Serializable
data class RfidCredentialResponse(
    val id: Long,
    val hexCode: String,
    val isBlocked: Boolean
)

@Serializable
data class CreateRfidCredentialRequest(
    val hexCode: String,
    val appUserId: Long
)

@Serializable
data class AccessLogResponse(
    val id: Long,
    val tagRead: String,
    val accessPointId: Long,
    val timestamp: String,
    val isGranted: Boolean,
    val denialReason: String? = null
)

// =================================================================================
// 4. CARDHOLDER DOMAIN
// =================================================================================

@Serializable
data class CardholderAccessLogResponse(
    val id: Long,
    val tagRead: String,
    val accessPointId: Long,
    val timestamp: String,
    val isGranted: Boolean,
    val denialReason: String? = null
)