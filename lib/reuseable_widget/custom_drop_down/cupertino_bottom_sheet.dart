// Copyright 2014 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:flutter/cupertino.dart' show CupertinoTheme, CupertinoApp;
import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart'
    show
        Colors,
        MaterialLocalizations,
        MaterialPageRoute,
        Theme,
        debugCheckHasMaterialLocalizations;
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

import 'bottom_sheet.dart';
import 'bottom_sheet_route.dart';

const double _behind_widget_visible_height = 10;

const Radius _default_top_radius = Radius.circular(12);

/// Cupertino Bottom Sheet Container
///
/// Clip the child widget to rectangle with top rounded corners and adds
/// top padding(+safe area padding). This padding [_behind_widget_visible_height]
/// is the height that will be displayed from previous route.
class _CupertinoBottomSheetContainer extends StatelessWidget {
  final Widget? child;
  final Color? backgroundColor;
  final Radius? topRadius;

  const _CupertinoBottomSheetContainer(
      {Key? key, this.child, this.backgroundColor, required this.topRadius})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    final topSafeAreaPadding = MediaQuery.of(context).padding.top;
    final topPadding = _behind_widget_visible_height + topSafeAreaPadding;

    final shadow =
        BoxShadow(blurRadius: 10, color: Colors.black12, spreadRadius: 5);
    final _backgroundColor =
        backgroundColor ?? CupertinoTheme.of(context).scaffoldBackgroundColor;
    return Padding(
      padding: EdgeInsets.only(top: topPadding),
      child: ClipRRect(
        borderRadius: BorderRadius.vertical(top: topRadius!),
        child: Container(
          decoration:
              BoxDecoration(color: _backgroundColor, boxShadow: [shadow]),
          width: double.infinity,
          child: MediaQuery.removePadding(
            context: context,
            removeTop: true, //Remove top Safe Area
            child: child!,
          ),
        ),
      ),
    );
  }
}

Future<T?> showCupertinoModalBottomSheet<T>({
  required BuildContext context,
  required WidgetBuilder builder,
  Color? backgroundColor,
  double? elevation,
  double? closeProgressThreshold,
  ShapeBorder? shape,
  Clip? clipBehavior,
  Color? barrierColor,
  bool expand = false,
  AnimationController? secondAnimation,
  Curve? animationCurve,
  Curve? previousRouteAnimationCurve,
  bool useRootNavigator = false,
  bool bounce = true,
  bool? isDismissible,
  bool enableDrag = true,
  Radius topRadius = _default_top_radius,
  Duration? duration,
  RouteSettings? settings,
  Color? transitionBackgroundColor,
}) async {
  assert(context != null);
  assert(builder != null);
  assert(expand != null);
  assert(useRootNavigator != null);
  assert(enableDrag != null);
  assert(debugCheckHasMediaQuery(context));
  final hasMaterialLocalizations =
      Localizations.of<MaterialLocalizations>(context, MaterialLocalizations) !=
          null;
  final barrierLabel = hasMaterialLocalizations
      ? MaterialLocalizations.of(context).modalBarrierDismissLabel
      : '';
  // final result = await Navigator.of(context, rootNavigator: useRootNavigator)
  //     .push(MaterialPageRoute(builder: builder));
  final result =
      await Navigator.of(context, rootNavigator: useRootNavigator).push(
    CupertinoModalBottomSheetRoute<T>(
        builder: builder,
        containerBuilder: (context, _, child) => _CupertinoBottomSheetContainer(
              backgroundColor: backgroundColor,
              topRadius: topRadius,
              child: child,
            ),
        secondAnimationController: secondAnimation,
        expanded: expand,
        closeProgressThreshold: closeProgressThreshold,
        barrierLabel: barrierLabel,
        elevation: elevation,
        bounce: bounce,
        shape: shape,
        clipBehavior: clipBehavior,
        isDismissible: isDismissible ?? expand == false ? true : false,
        modalBarrierColor: barrierColor ?? Colors.black12,
        enableDrag: enableDrag,
        topRadius: topRadius,
        animationCurve: animationCurve,
        previousRouteAnimationCurve: previousRouteAnimationCurve,
        duration: duration,
        settings: settings,
        transitionBackgroundColor: transitionBackgroundColor ?? Colors.black),
  );
  return result;
}

class CupertinoModalBottomSheetRoute<T> extends ModalBottomSheetRoute<T> {
  final Radius? topRadius;
  final Curve? previousRouteAnimationCurve;

  // Background color behind all routes
  // Black by default
  final Color? transitionBackgroundColor;

  CupertinoModalBottomSheetRoute({
    WidgetBuilder? builder,
    WidgetWithChildBuilder? containerBuilder,
    double? closeProgressThreshold,
    String? barrierLabel,
    double? elevation,
    ShapeBorder? shape,
    Clip? clipBehavior,
    AnimationController? secondAnimationController,
    Curve? animationCurve,
    Color? modalBarrierColor,
    bool bounce = true,
    bool isDismissible = true,
    bool enableDrag = true,
    required bool expanded,
    Duration? duration,
    RouteSettings? settings,
    ScrollController? scrollController,
    this.transitionBackgroundColor,
    this.topRadius = _default_top_radius,
    this.previousRouteAnimationCurve,
  })  : assert(expanded != null),
        assert(isDismissible != null),
        assert(enableDrag != null),
        super(
          closeProgressThreshold: closeProgressThreshold,
          scrollController: scrollController,
          containerBuilder: containerBuilder,
          builder: builder,
          bounce: bounce,
          barrierLabel: barrierLabel,
          secondAnimationController: secondAnimationController,
          modalBarrierColor: modalBarrierColor,
          isDismissible: isDismissible,
          enableDrag: enableDrag,
          expanded: expanded,
          settings: settings,
          animationCurve: animationCurve,
          duration: duration,
        );

  @override
  Widget buildTransitions(
    BuildContext context,
    Animation<double> animation,
    Animation<double> secondaryAnimation,
    Widget child,
  ) {
    // print("$child MIMI");
    // child = Container();
    final paddingTop = MediaQuery.of(context).padding.top;
    final distanceWithScale =
        (paddingTop + _behind_widget_visible_height) * 0.9;
    final offsetY = secondaryAnimation.value * (paddingTop - distanceWithScale);
    final scale = 1 - secondaryAnimation.value / 10;
    return AnimatedBuilder(
      builder: (context, child) => Transform.translate(
        offset: Offset(0, offsetY),
        child: Transform.scale(
          scale: scale,
          alignment: Alignment.topCenter,
          child: child,
        ),
      ),
      animation: secondaryAnimation,
      child: child,
    );
  }

  @override
  Widget getPreviousRouteTransition(BuildContext context,
      Animation<double> secondaryAnimation, Widget child) {
    return _CupertinoModalTransition(
      secondaryAnimation: secondaryAnimation,
      body: child,
      animationCurve: previousRouteAnimationCurve!,
      topRadius: topRadius!,
      backgroundColor: transitionBackgroundColor ?? Colors.black,
    );
  }
}

class _CupertinoModalTransition extends StatelessWidget {
  final Animation<double> secondaryAnimation;
  final Radius? topRadius;
  final Curve? animationCurve;
  final Color? backgroundColor;

  final Widget? body;

  const _CupertinoModalTransition({
    Key? key,
    required this.secondaryAnimation,
    required this.body,
    required this.topRadius,
    this.backgroundColor = Colors.black,
    this.animationCurve,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    var startRoundCorner = 0.0;
    final paddingTop = MediaQuery.of(context).padding.top;
    if (Theme.of(context).platform == TargetPlatform.iOS && paddingTop > 20) {
      startRoundCorner = 38.5;
      //https://kylebashour.com/posts/finding-the-real-iphone-x-corner-radius
    }

    final curvedAnimation = CurvedAnimation(
      parent: secondaryAnimation,
      curve: animationCurve ?? Curves.easeOut,
    );

    return AnnotatedRegion<SystemUiOverlayStyle>(
      value: SystemUiOverlayStyle.light,
      child: AnimatedBuilder(
        animation: curvedAnimation,
        child: body,
        builder: (context, child) {
          final progress = curvedAnimation.value;
          final yOffset = progress * paddingTop;
          final scale = 1 - progress / 10;
          final radius = progress == 0
              ? 0.0
              : (1 - progress) * startRoundCorner + progress * topRadius!.x;
          return Stack(
            children: <Widget>[
              Container(color: backgroundColor),
              Transform.translate(
                offset: Offset(0, yOffset),
                child: Transform.scale(
                  scale: scale,
                  alignment: Alignment.topCenter,
                  child: ClipRRect(
                      borderRadius: BorderRadius.circular(radius),
                      child: child),
                ),
              ),
            ],
          );
        },
      ),
    );
  }
}

class _CupertinoScaffold extends InheritedWidget {
  final AnimationController? animation;

  final Radius? topRadius;

  @override
  final Widget child;

  const _CupertinoScaffold(
      {Key? key, this.animation, required this.child, this.topRadius})
      : super(key: key, child: child);

  @override
  bool updateShouldNotify(InheritedWidget oldWidget) {
    return false;
  }
}

// Support
class CupertinoScaffold extends StatefulWidget {
  static _CupertinoScaffold? of(BuildContext context) =>
      context.dependOnInheritedWidgetOfExactType<_CupertinoScaffold>();

  final Widget? body;
  final Radius? topRadius;
  final Color? transitionBackgroundColor;

  const CupertinoScaffold({
    Key? key,
    this.body,
    this.topRadius = _default_top_radius,
    this.transitionBackgroundColor = Colors.black,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() => _CupertinoScaffoldState();

  static Future<T> showCupertinoModalBottomSheet<T>({
    required BuildContext context,
    double? closeProgressThreshold,
    required WidgetBuilder builder,
    Curve? animationCurve,
    Curve? previousRouteAnimationCurve,
    Color? backgroundColor,
    Color? barrierColor,
    bool expand = false,
    bool useRootNavigator = false,
    bool bounce = true,
    bool? isDismissible,
    bool enableDrag = true,
    Duration? duration,
    RouteSettings? settings,
  }) async {
    assert(context != null);
    assert(builder != null);
    assert(expand != null);
    assert(useRootNavigator != null);
    assert(enableDrag != null);
    assert(debugCheckHasMediaQuery(context));
    final isCupertinoApp =
        context.findAncestorWidgetOfExactType<CupertinoApp>() != null;
    var barrierLabel = '';
    if (!isCupertinoApp) {
      assert(debugCheckHasMaterialLocalizations(context));
      barrierLabel = MaterialLocalizations.of(context).modalBarrierDismissLabel;
    }
    final topRadius = CupertinoScaffold.of(context)!.topRadius;
    final result = await Navigator.of(context, rootNavigator: useRootNavigator)
        .push(MaterialPageRoute(
      builder: builder,
    ));
    // final result = await Navigator.of(context, rootNavigator: useRootNavigator)
    //     .push(CupertinoModalBottomSheetRoute<T>(
    //   closeProgressThreshold: closeProgressThreshold,
    //   builder: builder,
    //   secondAnimationController: CupertinoScaffold.of(context)!.animation,
    //   containerBuilder: (context, _, child) => _CupertinoBottomSheetContainer(
    //     backgroundColor: backgroundColor,
    //     topRadius: topRadius,
    //     child: child,
    //   ),
    //   expanded: expand,
    //   barrierLabel: barrierLabel,
    //   bounce: bounce,
    //   isDismissible: isDismissible ?? expand == false ? true : false,
    //   modalBarrierColor: barrierColor ?? Colors.black12,
    //   enableDrag: enableDrag,
    //   topRadius: topRadius,
    //   animationCurve: animationCurve,
    //   previousRouteAnimationCurve: previousRouteAnimationCurve,
    //   duration: duration,
    //   settings: settings,
    // ));
    return result!;
  }
}

class _CupertinoScaffoldState extends State<CupertinoScaffold>
    with TickerProviderStateMixin {
  AnimationController? animationController;

  SystemUiOverlayStyle? lastStyle;

  @override
  void initState() {
    animationController =
        AnimationController(duration: Duration(milliseconds: 350), vsync: this);
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return _CupertinoScaffold(
      animation: animationController,
      topRadius: widget.topRadius,
      child: _CupertinoModalTransition(
        secondaryAnimation: animationController!,
        body: widget.body,
        topRadius: widget.topRadius,
        backgroundColor: widget.transitionBackgroundColor,
      ),
    );
  }
}
