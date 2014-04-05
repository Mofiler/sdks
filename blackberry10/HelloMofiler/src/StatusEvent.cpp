/*#include "StatusEvent.h"
#include <bps/bps.h>
#include <bps/netstatus.h>

StatusEvent::StatusEvent()
{
    subscribe(netstatus_get_domain());

    bps_initialize();

    netstatus_request_events(0);
}

StatusEvent::~StatusEvent()
{
    bps_shutdown();
}

void StatusEvent::event(bps_event_t *event)
{
    bool status = false;
    const char* interface = "";
    const char* type = "asset:///images/unknown.png";

    netstatus_interface_details_t *details;

    if (bps_event_get_domain(event) == netstatus_get_domain())
    {
        if (NETSTATUS_INFO == bps_event_get_code(event))
        {
            status = netstatus_event_get_availability(event);
            interface = netstatus_event_get_default_interface(event);

            int success = netstatus_get_interface_details(interface, &details);
            if (success == BPS_SUCCESS)
            {
                /*switch (netstatus_interface_get_type(details))
                {
                    case NETSTATUS_INTERFACE_TYPE_WIFI:
                        type = "asset:///images/wifi.png";
                        break;

                    case NETSTATUS_INTERFACE_TYPE_BLUETOOTH_DUN:
                        type = "asset:///images/bluetooth.png";
                        break;

                    case NETSTATUS_INTERFACE_TYPE_CELLULAR:
                        type = "asset:///images/cellular.png";
                        break;

                    case NETSTATUS_INTERFACE_TYPE_UNKNOWN:
                        type = "asset:///images/unknown.png";
                        break;
                }*/
/*
                netstatus_free_interface_details(&details);
            }

            // Emit the signal to trigger networkStatusUpdated slot.
            emit networkStatusUpdated(status, type);
        }
    }
}
*/
