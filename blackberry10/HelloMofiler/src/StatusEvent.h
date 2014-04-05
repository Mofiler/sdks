/*#ifndef STATUSEVENT_H_
#define STATUSEVENT_H_

#include <QObject>
#include <QString>
#include <bb/AbstractBpsEventHandler>

class StatusEvent: public QObject, public bb::AbstractBpsEventHandler
{
    Q_OBJECT

public:
    StatusEvent();
    virtual ~StatusEvent();
    virtual void event(bps_event_t *event);

signals:
    void networkStatusUpdated(bool status, QString type);
};

#endif /* STATUSEVENT_H_ */
