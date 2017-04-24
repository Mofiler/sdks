#include "il2cpp-config.h"

#ifndef _MSC_VER
# include <alloca.h>
#else
# include <malloc.h>
#endif

#include <cstring>
#include <string.h>
#include <stdio.h>
#include <cmath>
#include <limits>
#include <assert.h>

// MofilerBridge
struct MofilerBridge_t1671278367;
// System.String
struct String_t;
// MofilerManager
struct MofilerManager_t2307651695;

#include "class-internals.h"
#include "codegen/il2cpp-codegen.h"
#include "mscorlib_System_Array3829468939.h"
#include "AssemblyU2DCSharp_U3CModuleU3E3783534214.h"
#include "AssemblyU2DCSharp_U3CModuleU3E3783534214MethodDeclarations.h"
#include "AssemblyU2DCSharp_MofilerBridge1671278367.h"
#include "AssemblyU2DCSharp_MofilerBridge1671278367MethodDeclarations.h"
#include "mscorlib_System_Void1841601450.h"
#include "mscorlib_System_Object2689449295MethodDeclarations.h"
#include "mscorlib_System_String2029220233.h"
#include "UnityEngine_UnityEngine_Application354826772MethodDeclarations.h"
#include "UnityEngine_UnityEngine_Debug1368543263MethodDeclarations.h"
#include "UnityEngine_UnityEngine_RuntimePlatform1869584967.h"
#include "mscorlib_System_Object2689449295.h"
#include "mscorlib_System_Boolean3825574718.h"
#include "AssemblyU2DCSharp_MofilerManager2307651695.h"
#include "AssemblyU2DCSharp_MofilerManager2307651695MethodDeclarations.h"
#include "UnityEngine_UnityEngine_MonoBehaviour1158329972MethodDeclarations.h"

#ifdef __clang__
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Winvalid-offsetof"
#pragma clang diagnostic ignored "-Wunused-variable"
#endif
// System.Void MofilerBridge::.ctor()
extern "C"  void MofilerBridge__ctor_m3646886380 (MofilerBridge_t1671278367 * __this, const MethodInfo* method)
{
	{
		Object__ctor_m2551263788(__this, /*hidden argument*/NULL);
		return;
	}
}
extern "C" void DEFAULT_CALL _SetURL(char*);
// System.Void MofilerBridge::_SetURL(System.String)
extern "C"  void MofilerBridge__SetURL_m2140879994 (Il2CppObject * __this /* static, unused */, String_t* ___urlString0, const MethodInfo* method)
{
	typedef void (DEFAULT_CALL *PInvokeFunc) (char*);

	// Marshaling of parameter '___urlString0' to native representation
	char* ____urlString0_marshaled = NULL;
	____urlString0_marshaled = il2cpp_codegen_marshal_string(___urlString0);

	// Native function invocation
	reinterpret_cast<PInvokeFunc>(_SetURL)(____urlString0_marshaled);

	// Marshaling cleanup of parameter '___urlString0' native representation
	il2cpp_codegen_marshal_free(____urlString0_marshaled);
	____urlString0_marshaled = NULL;

}
// System.Void MofilerBridge::SetURL(System.String)
extern Il2CppClass* Debug_t1368543263_il2cpp_TypeInfo_var;
extern Il2CppCodeGenString* _stringLiteral269412898;
extern const uint32_t MofilerBridge_SetURL_m3409003665_MetadataUsageId;
extern "C"  void MofilerBridge_SetURL_m3409003665 (Il2CppObject * __this /* static, unused */, String_t* ___urlString0, const MethodInfo* method)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_method (MofilerBridge_SetURL_m3409003665_MetadataUsageId);
		s_Il2CppMethodInitialized = true;
	}
	{
		int32_t L_0 = Application_get_platform_m3989224144(NULL /*static, unused*/, /*hidden argument*/NULL);
		if (!L_0)
		{
			goto IL_0016;
		}
	}
	{
		String_t* L_1 = ___urlString0;
		MofilerBridge__SetURL_m2140879994(NULL /*static, unused*/, L_1, /*hidden argument*/NULL);
		goto IL_0020;
	}

IL_0016:
	{
		IL2CPP_RUNTIME_CLASS_INIT(Debug_t1368543263_il2cpp_TypeInfo_var);
		Debug_Log_m920475918(NULL /*static, unused*/, _stringLiteral269412898, /*hidden argument*/NULL);
	}

IL_0020:
	{
		return;
	}
}
extern "C" void DEFAULT_CALL _SetAppKey(char*);
// System.Void MofilerBridge::_SetAppKey(System.String)
extern "C"  void MofilerBridge__SetAppKey_m3704987031 (Il2CppObject * __this /* static, unused */, String_t* ___appKey0, const MethodInfo* method)
{
	typedef void (DEFAULT_CALL *PInvokeFunc) (char*);

	// Marshaling of parameter '___appKey0' to native representation
	char* ____appKey0_marshaled = NULL;
	____appKey0_marshaled = il2cpp_codegen_marshal_string(___appKey0);

	// Native function invocation
	reinterpret_cast<PInvokeFunc>(_SetAppKey)(____appKey0_marshaled);

	// Marshaling cleanup of parameter '___appKey0' native representation
	il2cpp_codegen_marshal_free(____appKey0_marshaled);
	____appKey0_marshaled = NULL;

}
// System.Void MofilerBridge::SetAppKey(System.String)
extern Il2CppClass* Debug_t1368543263_il2cpp_TypeInfo_var;
extern Il2CppCodeGenString* _stringLiteral269412898;
extern const uint32_t MofilerBridge_SetAppKey_m1685134348_MetadataUsageId;
extern "C"  void MofilerBridge_SetAppKey_m1685134348 (Il2CppObject * __this /* static, unused */, String_t* ___appKey0, const MethodInfo* method)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_method (MofilerBridge_SetAppKey_m1685134348_MetadataUsageId);
		s_Il2CppMethodInitialized = true;
	}
	{
		int32_t L_0 = Application_get_platform_m3989224144(NULL /*static, unused*/, /*hidden argument*/NULL);
		if (!L_0)
		{
			goto IL_0016;
		}
	}
	{
		String_t* L_1 = ___appKey0;
		MofilerBridge__SetAppKey_m3704987031(NULL /*static, unused*/, L_1, /*hidden argument*/NULL);
		goto IL_0020;
	}

IL_0016:
	{
		IL2CPP_RUNTIME_CLASS_INIT(Debug_t1368543263_il2cpp_TypeInfo_var);
		Debug_Log_m920475918(NULL /*static, unused*/, _stringLiteral269412898, /*hidden argument*/NULL);
	}

IL_0020:
	{
		return;
	}
}
extern "C" void DEFAULT_CALL _FlushDataToMofiler();
// System.Void MofilerBridge::_FlushDataToMofiler()
extern "C"  void MofilerBridge__FlushDataToMofiler_m2069808264 (Il2CppObject * __this /* static, unused */, const MethodInfo* method)
{
	typedef void (DEFAULT_CALL *PInvokeFunc) ();

	// Native function invocation
	reinterpret_cast<PInvokeFunc>(_FlushDataToMofiler)();

}
// System.Void MofilerBridge::FlushDataToMofiler()
extern Il2CppClass* Debug_t1368543263_il2cpp_TypeInfo_var;
extern Il2CppCodeGenString* _stringLiteral269412898;
extern const uint32_t MofilerBridge_FlushDataToMofiler_m1071533831_MetadataUsageId;
extern "C"  void MofilerBridge_FlushDataToMofiler_m1071533831 (Il2CppObject * __this /* static, unused */, const MethodInfo* method)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_method (MofilerBridge_FlushDataToMofiler_m1071533831_MetadataUsageId);
		s_Il2CppMethodInitialized = true;
	}
	{
		int32_t L_0 = Application_get_platform_m3989224144(NULL /*static, unused*/, /*hidden argument*/NULL);
		if (!L_0)
		{
			goto IL_0015;
		}
	}
	{
		MofilerBridge__FlushDataToMofiler_m2069808264(NULL /*static, unused*/, /*hidden argument*/NULL);
		goto IL_001f;
	}

IL_0015:
	{
		IL2CPP_RUNTIME_CLASS_INIT(Debug_t1368543263_il2cpp_TypeInfo_var);
		Debug_Log_m920475918(NULL /*static, unused*/, _stringLiteral269412898, /*hidden argument*/NULL);
	}

IL_001f:
	{
		return;
	}
}
extern "C" void DEFAULT_CALL _SetAppName(char*);
// System.Void MofilerBridge::_SetAppName(System.String)
extern "C"  void MofilerBridge__SetAppName_m3910654479 (Il2CppObject * __this /* static, unused */, String_t* ___appName0, const MethodInfo* method)
{
	typedef void (DEFAULT_CALL *PInvokeFunc) (char*);

	// Marshaling of parameter '___appName0' to native representation
	char* ____appName0_marshaled = NULL;
	____appName0_marshaled = il2cpp_codegen_marshal_string(___appName0);

	// Native function invocation
	reinterpret_cast<PInvokeFunc>(_SetAppName)(____appName0_marshaled);

	// Marshaling cleanup of parameter '___appName0' native representation
	il2cpp_codegen_marshal_free(____appName0_marshaled);
	____appName0_marshaled = NULL;

}
// System.Void MofilerBridge::SetAppName(System.String)
extern Il2CppClass* Debug_t1368543263_il2cpp_TypeInfo_var;
extern Il2CppCodeGenString* _stringLiteral269412898;
extern const uint32_t MofilerBridge_SetAppName_m4263877880_MetadataUsageId;
extern "C"  void MofilerBridge_SetAppName_m4263877880 (Il2CppObject * __this /* static, unused */, String_t* ___appName0, const MethodInfo* method)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_method (MofilerBridge_SetAppName_m4263877880_MetadataUsageId);
		s_Il2CppMethodInitialized = true;
	}
	{
		int32_t L_0 = Application_get_platform_m3989224144(NULL /*static, unused*/, /*hidden argument*/NULL);
		if (!L_0)
		{
			goto IL_0016;
		}
	}
	{
		String_t* L_1 = ___appName0;
		MofilerBridge__SetAppName_m3910654479(NULL /*static, unused*/, L_1, /*hidden argument*/NULL);
		goto IL_0020;
	}

IL_0016:
	{
		IL2CPP_RUNTIME_CLASS_INIT(Debug_t1368543263_il2cpp_TypeInfo_var);
		Debug_Log_m920475918(NULL /*static, unused*/, _stringLiteral269412898, /*hidden argument*/NULL);
	}

IL_0020:
	{
		return;
	}
}
extern "C" void DEFAULT_CALL _AddIdentity(char*, char*);
// System.Void MofilerBridge::_AddIdentity(System.String,System.String)
extern "C"  void MofilerBridge__AddIdentity_m3871333438 (Il2CppObject * __this /* static, unused */, String_t* ___key0, String_t* ___value1, const MethodInfo* method)
{
	typedef void (DEFAULT_CALL *PInvokeFunc) (char*, char*);

	// Marshaling of parameter '___key0' to native representation
	char* ____key0_marshaled = NULL;
	____key0_marshaled = il2cpp_codegen_marshal_string(___key0);

	// Marshaling of parameter '___value1' to native representation
	char* ____value1_marshaled = NULL;
	____value1_marshaled = il2cpp_codegen_marshal_string(___value1);

	// Native function invocation
	reinterpret_cast<PInvokeFunc>(_AddIdentity)(____key0_marshaled, ____value1_marshaled);

	// Marshaling cleanup of parameter '___key0' native representation
	il2cpp_codegen_marshal_free(____key0_marshaled);
	____key0_marshaled = NULL;

	// Marshaling cleanup of parameter '___value1' native representation
	il2cpp_codegen_marshal_free(____value1_marshaled);
	____value1_marshaled = NULL;

}
// System.Void MofilerBridge::AddIdentity(System.String,System.String)
extern Il2CppClass* Debug_t1368543263_il2cpp_TypeInfo_var;
extern Il2CppCodeGenString* _stringLiteral269412898;
extern const uint32_t MofilerBridge_AddIdentity_m2793072069_MetadataUsageId;
extern "C"  void MofilerBridge_AddIdentity_m2793072069 (Il2CppObject * __this /* static, unused */, String_t* ___key0, String_t* ___value1, const MethodInfo* method)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_method (MofilerBridge_AddIdentity_m2793072069_MetadataUsageId);
		s_Il2CppMethodInitialized = true;
	}
	{
		int32_t L_0 = Application_get_platform_m3989224144(NULL /*static, unused*/, /*hidden argument*/NULL);
		if (!L_0)
		{
			goto IL_0017;
		}
	}
	{
		String_t* L_1 = ___key0;
		String_t* L_2 = ___value1;
		MofilerBridge__AddIdentity_m3871333438(NULL /*static, unused*/, L_1, L_2, /*hidden argument*/NULL);
		goto IL_0021;
	}

IL_0017:
	{
		IL2CPP_RUNTIME_CLASS_INIT(Debug_t1368543263_il2cpp_TypeInfo_var);
		Debug_Log_m920475918(NULL /*static, unused*/, _stringLiteral269412898, /*hidden argument*/NULL);
	}

IL_0021:
	{
		return;
	}
}
extern "C" void DEFAULT_CALL _InjectValue(char*, char*);
// System.Void MofilerBridge::_InjectValue(System.String,System.String)
extern "C"  void MofilerBridge__InjectValue_m1023643885 (Il2CppObject * __this /* static, unused */, String_t* ___key0, String_t* ___value1, const MethodInfo* method)
{
	typedef void (DEFAULT_CALL *PInvokeFunc) (char*, char*);

	// Marshaling of parameter '___key0' to native representation
	char* ____key0_marshaled = NULL;
	____key0_marshaled = il2cpp_codegen_marshal_string(___key0);

	// Marshaling of parameter '___value1' to native representation
	char* ____value1_marshaled = NULL;
	____value1_marshaled = il2cpp_codegen_marshal_string(___value1);

	// Native function invocation
	reinterpret_cast<PInvokeFunc>(_InjectValue)(____key0_marshaled, ____value1_marshaled);

	// Marshaling cleanup of parameter '___key0' native representation
	il2cpp_codegen_marshal_free(____key0_marshaled);
	____key0_marshaled = NULL;

	// Marshaling cleanup of parameter '___value1' native representation
	il2cpp_codegen_marshal_free(____value1_marshaled);
	____value1_marshaled = NULL;

}
// System.Void MofilerBridge::InjectValue(System.String,System.String)
extern Il2CppClass* Debug_t1368543263_il2cpp_TypeInfo_var;
extern Il2CppCodeGenString* _stringLiteral269412898;
extern const uint32_t MofilerBridge_InjectValue_m2590442440_MetadataUsageId;
extern "C"  void MofilerBridge_InjectValue_m2590442440 (Il2CppObject * __this /* static, unused */, String_t* ___key0, String_t* ___value1, const MethodInfo* method)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_method (MofilerBridge_InjectValue_m2590442440_MetadataUsageId);
		s_Il2CppMethodInitialized = true;
	}
	{
		int32_t L_0 = Application_get_platform_m3989224144(NULL /*static, unused*/, /*hidden argument*/NULL);
		if (!L_0)
		{
			goto IL_0017;
		}
	}
	{
		String_t* L_1 = ___key0;
		String_t* L_2 = ___value1;
		MofilerBridge__InjectValue_m1023643885(NULL /*static, unused*/, L_1, L_2, /*hidden argument*/NULL);
		goto IL_0021;
	}

IL_0017:
	{
		IL2CPP_RUNTIME_CLASS_INIT(Debug_t1368543263_il2cpp_TypeInfo_var);
		Debug_Log_m920475918(NULL /*static, unused*/, _stringLiteral269412898, /*hidden argument*/NULL);
	}

IL_0021:
	{
		return;
	}
}
extern "C" void DEFAULT_CALL _SetUseVerboseContext(int32_t);
// System.Void MofilerBridge::_SetUseVerboseContext(System.Boolean)
extern "C"  void MofilerBridge__SetUseVerboseContext_m3028933166 (Il2CppObject * __this /* static, unused */, bool ___state0, const MethodInfo* method)
{
	typedef void (DEFAULT_CALL *PInvokeFunc) (int32_t);

	// Native function invocation
	reinterpret_cast<PInvokeFunc>(_SetUseVerboseContext)(static_cast<int32_t>(___state0));

}
// System.Void MofilerBridge::SetUseVerboseContext(System.Boolean)
extern Il2CppClass* Debug_t1368543263_il2cpp_TypeInfo_var;
extern Il2CppCodeGenString* _stringLiteral269412898;
extern const uint32_t MofilerBridge_SetUseVerboseContext_m714003013_MetadataUsageId;
extern "C"  void MofilerBridge_SetUseVerboseContext_m714003013 (Il2CppObject * __this /* static, unused */, bool ___state0, const MethodInfo* method)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_method (MofilerBridge_SetUseVerboseContext_m714003013_MetadataUsageId);
		s_Il2CppMethodInitialized = true;
	}
	{
		int32_t L_0 = Application_get_platform_m3989224144(NULL /*static, unused*/, /*hidden argument*/NULL);
		if (!L_0)
		{
			goto IL_0016;
		}
	}
	{
		bool L_1 = ___state0;
		MofilerBridge__SetUseVerboseContext_m3028933166(NULL /*static, unused*/, L_1, /*hidden argument*/NULL);
		goto IL_0020;
	}

IL_0016:
	{
		IL2CPP_RUNTIME_CLASS_INIT(Debug_t1368543263_il2cpp_TypeInfo_var);
		Debug_Log_m920475918(NULL /*static, unused*/, _stringLiteral269412898, /*hidden argument*/NULL);
	}

IL_0020:
	{
		return;
	}
}
extern "C" void DEFAULT_CALL _SetUserLocation(int32_t);
// System.Void MofilerBridge::_SetUserLocation(System.Boolean)
extern "C"  void MofilerBridge__SetUserLocation_m3977527760 (Il2CppObject * __this /* static, unused */, bool ___state0, const MethodInfo* method)
{
	typedef void (DEFAULT_CALL *PInvokeFunc) (int32_t);

	// Native function invocation
	reinterpret_cast<PInvokeFunc>(_SetUserLocation)(static_cast<int32_t>(___state0));

}
// System.Void MofilerBridge::SetUserLocation(System.Boolean)
extern Il2CppClass* Debug_t1368543263_il2cpp_TypeInfo_var;
extern Il2CppCodeGenString* _stringLiteral269412898;
extern const uint32_t MofilerBridge_SetUserLocation_m203518127_MetadataUsageId;
extern "C"  void MofilerBridge_SetUserLocation_m203518127 (Il2CppObject * __this /* static, unused */, bool ___state0, const MethodInfo* method)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_method (MofilerBridge_SetUserLocation_m203518127_MetadataUsageId);
		s_Il2CppMethodInitialized = true;
	}
	{
		int32_t L_0 = Application_get_platform_m3989224144(NULL /*static, unused*/, /*hidden argument*/NULL);
		if (!L_0)
		{
			goto IL_0016;
		}
	}
	{
		bool L_1 = ___state0;
		MofilerBridge__SetUserLocation_m3977527760(NULL /*static, unused*/, L_1, /*hidden argument*/NULL);
		goto IL_0020;
	}

IL_0016:
	{
		IL2CPP_RUNTIME_CLASS_INIT(Debug_t1368543263_il2cpp_TypeInfo_var);
		Debug_Log_m920475918(NULL /*static, unused*/, _stringLiteral269412898, /*hidden argument*/NULL);
	}

IL_0020:
	{
		return;
	}
}
// System.Void MofilerManager::.ctor()
extern "C"  void MofilerManager__ctor_m748125042 (MofilerManager_t2307651695 * __this, const MethodInfo* method)
{
	{
		MonoBehaviour__ctor_m2464341955(__this, /*hidden argument*/NULL);
		return;
	}
}
// System.Void MofilerManager::Start()
extern "C"  void MofilerManager_Start_m164776174 (MofilerManager_t2307651695 * __this, const MethodInfo* method)
{
	{
		return;
	}
}
// System.Void MofilerManager::Update()
extern "C"  void MofilerManager_Update_m2134793915 (MofilerManager_t2307651695 * __this, const MethodInfo* method)
{
	{
		return;
	}
}
// System.Void MofilerManager::OnButtonTest()
extern Il2CppClass* Debug_t1368543263_il2cpp_TypeInfo_var;
extern Il2CppCodeGenString* _stringLiteral3840347794;
extern Il2CppCodeGenString* _stringLiteral4184469800;
extern Il2CppCodeGenString* _stringLiteral71896364;
extern Il2CppCodeGenString* _stringLiteral3726277302;
extern Il2CppCodeGenString* _stringLiteral18424612;
extern Il2CppCodeGenString* _stringLiteral339799447;
extern Il2CppCodeGenString* _stringLiteral3899715745;
extern Il2CppCodeGenString* _stringLiteral395217037;
extern Il2CppCodeGenString* _stringLiteral3709742221;
extern Il2CppCodeGenString* _stringLiteral536379538;
extern Il2CppCodeGenString* _stringLiteral353958301;
extern Il2CppCodeGenString* _stringLiteral677542039;
extern Il2CppCodeGenString* _stringLiteral508713991;
extern const uint32_t MofilerManager_OnButtonTest_m3083630459_MetadataUsageId;
extern "C"  void MofilerManager_OnButtonTest_m3083630459 (MofilerManager_t2307651695 * __this, const MethodInfo* method)
{
	static bool s_Il2CppMethodInitialized;
	if (!s_Il2CppMethodInitialized)
	{
		il2cpp_codegen_initialize_method (MofilerManager_OnButtonTest_m3083630459_MetadataUsageId);
		s_Il2CppMethodInitialized = true;
	}
	{
		MofilerBridge_SetAppKey_m1685134348(NULL /*static, unused*/, _stringLiteral3840347794, /*hidden argument*/NULL);
		MofilerBridge_SetAppName_m4263877880(NULL /*static, unused*/, _stringLiteral4184469800, /*hidden argument*/NULL);
		MofilerBridge_SetURL_m3409003665(NULL /*static, unused*/, _stringLiteral71896364, /*hidden argument*/NULL);
		MofilerBridge_SetUseVerboseContext_m714003013(NULL /*static, unused*/, (bool)1, /*hidden argument*/NULL);
		MofilerBridge_SetUserLocation_m203518127(NULL /*static, unused*/, (bool)1, /*hidden argument*/NULL);
		MofilerBridge_AddIdentity_m2793072069(NULL /*static, unused*/, _stringLiteral3726277302, _stringLiteral18424612, /*hidden argument*/NULL);
		MofilerBridge_AddIdentity_m2793072069(NULL /*static, unused*/, _stringLiteral339799447, _stringLiteral3899715745, /*hidden argument*/NULL);
		MofilerBridge_InjectValue_m2590442440(NULL /*static, unused*/, _stringLiteral395217037, _stringLiteral3709742221, /*hidden argument*/NULL);
		MofilerBridge_InjectValue_m2590442440(NULL /*static, unused*/, _stringLiteral536379538, _stringLiteral353958301, /*hidden argument*/NULL);
		MofilerBridge_InjectValue_m2590442440(NULL /*static, unused*/, _stringLiteral677542039, _stringLiteral353958301, /*hidden argument*/NULL);
		MofilerBridge_FlushDataToMofiler_m1071533831(NULL /*static, unused*/, /*hidden argument*/NULL);
		IL2CPP_RUNTIME_CLASS_INIT(Debug_t1368543263_il2cpp_TypeInfo_var);
		Debug_Log_m920475918(NULL /*static, unused*/, _stringLiteral508713991, /*hidden argument*/NULL);
		return;
	}
}
#ifdef __clang__
#pragma clang diagnostic pop
#endif
