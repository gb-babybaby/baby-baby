/* mypage-follow.js */

$(document).on('click', '.following-btn', function() {
    if ($(this).hasClass('following-btn-not')) {
        $(this).addClass('following-btn').removeClass('following-btn-not');
        $(this).css('color', 'black');
        $(this).css('border', '1px #cdd3d8 solid');
        $(this).children().children().attr('viewBox', '0 0 48 48');
        $(this).children().children().children().attr('d', 'M18 39.6L4.8 26.4l3.36-3.36L18 32.76l21.84-21.72 3.36 3.36z');
        $(this).children().children().children().attr('fill', 'black');
    } else {
        $(this).addClass('following-btn-not').removeClass('following-btn');
        $(this).css('color', 'red');
        $(this).css('border', '1px red solid');
        $(this).children().children().attr('viewBox', '0 0 32 32');
        $(this).children().children().children().attr('d', 'M30.4 15.2H16.8V1.6h-1.6v13.6H1.6v1.6h13.6v13.6h1.6V16.8h13.6v-1.6z');
        $(this).children().children().children().attr('fill', 'red');
    }
});

  globalThis.page = 0;

let followService =(() =>{

    function getFollowers(callback) {
        $.ajax({
            url: `/follows/list/followers`,
            data: {"page" : globalThis.page},
            type: 'post',
            success: function (results) {
                console.log("ajax== getFollowers success");
                callback(results);
                attachFollowingButtonEvent();
            }
        });
    }

    function getFollowings(callback) {
        $.ajax({
            url: `/follows/list/followings`,
            data: {"page" : globalThis.page},
            type: 'post',
            success: function (results) {
                console.log("ajax== getFollowings success");
                callback(results);
            }
        });
    }

    function isFollowed(memberEmail) {
        $.ajax({
            url: `/follows/isFollowed`,
            data: {"memberEmail": memberEmail},
            type: 'post',
            success: function (results) {
                return results;
            }
        });
    }

    function countFollowers(memberEmail) {
        return new Promise(function (resolve, reject) {
            $.ajax({
                url: `/follows/countFollowers`,
                data: {"memberEmail": memberEmail},
                type: 'post',
                success: function (results) {
                    resolve(results);
                },
                error: function (error) {
                    reject(error);
                }
            });
        });
    }


    return {getFollowers : getFollowers , getFollowings : getFollowings, isFollowed : isFollowed, countFollowers : countFollowers}

})();

function attachFollowingButtonEvent() {
    $('.following-btn').off('click').on('click', function() {
        if ($(this).hasClass('following-btn-not')) {
            $(this).addClass('following-btn').removeClass('following-btn-not');
            $(this).css({
                'color': 'black',
                'border': '1px #cdd3d8 solid'
            });
            $(this).children().children().attr('viewBox', '0 0 48 48');
            $(this).children().children().children().attr('d', 'M18 39.6L4.8 26.4l3.36-3.36L18 32.76l21.84-21.72 3.36 3.36z');
            $(this).children().children().children().attr('fill', 'black');
        } else {
            $(this).addClass('following-btn-not').removeClass('following-btn');
            $(this).css({
                'color': 'red',
                'border': '1px red solid'
            });
            $(this).children().children().attr('viewBox', '0 0 32 32');
            $(this).children().children().children().attr('d', 'M30.4 15.2H16.8V1.6h-1.6v13.6H1.6v1.6h13.6v13.6h1.6V16.8h13.6v-1.6z');
            $(this).children().children().children().attr('fill', 'red');
        }
    });
}


function appendFollowingsList(results){
    let text = '';
    let promises = [];
    results.content.forEach(result => {
        let count = followService.countFollowers(result.memberEmail);
        let countPromise = followService.countFollowers(result.memberEmail);
        promises.push(countPromise);
        countPromise.then(count => {
            text += ` 
                       <div class="one-content"  onclick="location.href='/member/details/${result.id}'">
                                <div class="profile-area">
                                    <div class="profile">
                                        <img src="/members/display?fileName=Member/Profile/${result.memberProfilePath}/${result.memberProfileUUID}_${result.memberProfileOriginalName}">
                                    </div>
                                </div>
    
                                <div class="profile-name-area">
                                    <p class="name">${result.memberNickname}</p>
                                    <p class="follower-count">팔로우 하는 사람 <span>${count}</span></p>
                                </div>
                        </div>
                        <div class="line"></div>`
        });
    });
    Promise.all(promises).then(() => {
        $('.one-content-wrapper').html(text);
    });
}

// 페이지 로딩 시 초기 리스트를 불러옴
followService.getFollowings(
    function (results) {
        appendFollowingsList(results);
    });