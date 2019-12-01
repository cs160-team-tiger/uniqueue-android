package tiger.uniqueue.data

import tiger.uniqueue.data.model.UserUiConf

enum class LoginType(val uiConf: UserUiConf) {
    STUDENT(UserUiConf.STUDENT),
    INSTRUCTOR(UserUiConf.INSTRUCTOR);
}