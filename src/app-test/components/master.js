import React from 'react';
import Title from 'react-title-component';
import AppBar from 'material-ui/AppBar';
import IconButton from 'material-ui/IconButton';
import IconMenu from 'material-ui/IconMenu';
import MenuItem from 'material-ui/MenuItem';
import MoreVertIcon from 'material-ui/svg-icons/navigation/more-vert';
import spacing from 'material-ui/styles/spacing';
import styleResizable from 'material-ui/utils/styleResizable';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import { colors } from 'material-ui/styles';
import { darkWhite, lightWhite, grey900 } from 'material-ui/styles/colors';




const Master = React.createClass({

    propTypes: {
        children: React.PropTypes.node,
        location: React.PropTypes.object,
    },

    contextTypes: {
        router: React.PropTypes.object.isRequired,
    },

    childContextTypes: {
        muiTheme: React.PropTypes.object,
    },

    mixins: [
        styleResizable
    ],

    getInitialState() {
        return {
            muiTheme: getMuiTheme(),
            navDrawerOpen: true,
        };
    },

    getChildContext() {
        return {
            muiTheme: this.state.muiTheme,
        };
    },

    componentWillMount() {
        this.setState({
            muiTheme: this.state.muiTheme,
        });
    },

    componentWillReceiveProps(nextProps, nextContext) {
        const newMuiTheme = nextContext.muiTheme ? nextContext.muiTheme : this.state.muiTheme;
        this.setState({
            muiTheme: newMuiTheme,
        });
    },

    getStyles() {
        const styles = {
            appBar: {
                position: 'fixed',
                // Needed to overlap the examples
                zIndex: this.state.muiTheme.zIndex.appBar + 1,
                top: 0,
            },
            root: {
                paddingTop: spacing.desktopKeylineIncrement,
                minHeight: 400,
            },
            content: {
                margin: spacing.desktopGutter,
            },
            contentWhenMedium: {
                margin: `${spacing.desktopGutter * 2}px ${spacing.desktopGutter * 3}px`,
            },
            footer: {
                backgroundColor: grey900,
                textAlign: 'center',
            },
            a: {
                color: darkWhite,
            },
            p: {
                margin: '0 auto',
                padding: 0,
                color: lightWhite,
                maxWidth: 356,
            },
            iconButton: {
                color: darkWhite,
            }
        };

        if (this.isDeviceSize(styleResizable.statics.Sizes.MEDIUM) ||
                this.isDeviceSize(styleResizable.statics.Sizes.LARGE)) {
            styles.content = Object.assign(styles.content, styles.contentWhenMedium);
        }

        return styles;
    },




    render() {
        const {location, children} = this.props;

        let {navDrawerOpen} = this.state;

        const {prepareStyles} = this.state.muiTheme;

        const router = this.context.router;
        const styles = this.getStyles();


        let docked = false;
        let showMenuIconButton = true;

        if (this.isDeviceSize(styleResizable.statics.Sizes.LARGE) && title !== '') {
            docked = true;
            navDrawerOpen = true;
            showMenuIconButton = false;

            styles.navDrawer = {
                zIndex: styles.appBar.zIndex - 1,
            };
            styles.root.paddingLeft = 256;
            styles.footer.paddingLeft = 256;
        }

        return (
            <div>
        <Title render="Material-UI" />
        <AppBar
            onLeftIconButtonTouchTap={this.handleTouchTapLeftIconButton}
            title='佛山气象监测预警系统'
            zDepth={0}
            iconElementLeft={ <img src='images/logo-48.png' /> }
            iconElementRight={ <div style={{
                float: 'left'
            }}>
            <div className={'menu'}>
                <ul><li><a>天气雷达</a></li>
            <li>
            <IconMenu
            onItemTouchTap={ this.handleMenuItemTap }
            iconButtonElement={ <IconButton className='hint--bottom'  data-hint='设置'>
            <MoreVertIcon />
            </IconButton> }
            targetOrigin={{
                horizontal: 'left',
                vertical: 'top'
            }}
            anchorOrigin={{
                horizontal: 'right',
                vertical: 'top'
            }}>
                                           <MenuItem value='light' leftIcon={ <ActionFavorite color={ colors.cyanA200 } hoverColor={ colors.orange300 } /> } primaryText='浅色主题' />
                                           <MenuItem value='dark' leftIcon={ <ActionFavorite color={ colors.cyanA700 } hoverColor={ colors.orange300 } /> } primaryText='深色主题' />
                                           <Divider />
                                           <MenuItem value='doc' leftIcon={ <ContentDrafts color={ colors.greenA400 } hoverColor={ colors.orange300 } /> } primaryText='说明文档' />
                                         </IconMenu>
                                       </li>
                                     </ul>
                                   </div>
                                 </div>
            }
            />
        style = { styles.appBar }
        showMenuIconButton = { showMenuIconButton }
        /> {
            title !== '' ?
                <div style={prepareStyles(styles.root)}>
            <div style={prepareStyles(styles.content)}>
              {React.cloneElement(children, {
                    onChangeMuiTheme: this.handleChangeMuiTheme,
                })}
            </div>
          </div> :
                children
            }
        <AppNavDrawer
            style={styles.navDrawer}
            location={location}
            docked={docked}
            onRequestChangeNavDrawer={this.handleChangeRequestNavDrawer}
            onChangeList={this.handleChangeList}
            open={navDrawerOpen}
            /> < FullWidthSection style = { styles.footer } >
            <p style={prepareStyles(styles.p)}>
            {'Hand crafted with love by the engineers at '}
            <a style={styles.a} href="http://www.call-em-all.com/Careers">
              Call-Em-All
            </a>
            {' and our awesome '}
            <a
            style={prepareStyles(styles.a)}
            href="https://github.com/callemall/material-ui/graphs/contributors"
            >
              contributors
            </a>.
          </p> < IconButton
            iconStyle = { styles.iconButton }
            iconClassName = "muidocs-icon-custom-github"
            href = "https://github.com/callemall/material-ui"
            linkButton = { true }
            /> < /FullWidthSection> < /div>
            );
    }







});

export default Master;
