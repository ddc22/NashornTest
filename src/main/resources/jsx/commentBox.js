var CommentBox = React.createClass({
    render: function() {
        return (
            <div className="commentBox">
                Hello, world! I am a CommentBox.
            </div>
        );
    }
});

var renderServer = function (comments) {
    return React.renderToString(
        <CommentBox />
    );
};