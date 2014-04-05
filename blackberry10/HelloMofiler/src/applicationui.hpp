// Default empty project template
#ifndef ApplicationUI_HPP_
#define ApplicationUI_HPP_

#include <QObject>
#include "Mofiler.h"

namespace bb { namespace cascades { class Application; }}
using namespace MofilerSDK;

/*!
 * @brief Application pane object
 *
 *Use this object to create and init app UI, to create context objects, to register the new meta types etc.
 */
class ApplicationUI : public QObject
{
    Q_OBJECT
public:
    ApplicationUI(bb::cascades::Application *app);
    virtual ~ApplicationUI() { if (mofObject) delete mofObject;}

private:
    Mofiler * mofObject;
};


#endif /* ApplicationUI_HPP_ */
