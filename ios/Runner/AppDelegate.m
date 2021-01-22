#import "AppDelegate.h"
#import "GeneratedPluginRegistrant.h"
#import "pigeon.h"
#import <Flutter/Flutter.h>

@interface MyApi : NSObject <Api>

@end

@implementation MyApi

- (SearchReply *)searchReply:(SearchRequest *)input error:(FlutterError * _Nullable __autoreleasing *)error {
    SearchReply *reply = [[SearchReply alloc] init];
    
    int batteryLevel = [self getBatteryLevel];
    NSString *result = @"not result";
    if (batteryLevel != -1) {
        result = [NSString stringWithFormat:@"Hi %@! The current battery level is %d %%", input.query, batteryLevel];
    } else {
        result = [NSString stringWithFormat:@"Hi %@! Battery level not available.", input.query];
    }
    reply.result = result;
    return reply;
}

- (int)getBatteryLevel {
  UIDevice* device = UIDevice.currentDevice;
  device.batteryMonitoringEnabled = YES;
  if (device.batteryState == UIDeviceBatteryStateUnknown) {
    return -1;
  } else {
    return (int)(device.batteryLevel * 100);
  }
}

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {

    FlutterViewController* controller = (FlutterViewController*)self.window.rootViewController;
    
    MyApi *api = [[MyApi alloc] init];
    ApiSetup(controller.binaryMessenger, api);

    [GeneratedPluginRegistrant registerWithRegistry:self];
    // Override point for customization after application launch.
    return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

@end
